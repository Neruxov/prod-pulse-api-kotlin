package ru.prodcontest.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.prodcontest.data.countries.repo.CountryRepository
import ru.prodcontest.data.token.enums.TokenType
import ru.prodcontest.data.token.model.Token
import ru.prodcontest.data.token.repo.TokenRepository
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.repo.UserRepository
import ru.prodcontest.data.user.request.RegisterRequest
import ru.prodcontest.data.user.request.SignInRequest
import ru.prodcontest.data.user.response.RegisterResponse
import ru.prodcontest.data.user.response.SignInResponse
import ru.prodcontest.exception.type.StatusCodeException

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class AuthService(
    val tokenRepository: TokenRepository,
    val userRepository: UserRepository,
    val jwtService: JwtService,
    val countryRepository: CountryRepository,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager
) {

    fun signIn(body: SignInRequest): Any {
        val user = userRepository.findByLogin(body.login)
            .orElseThrow { StatusCodeException(401, "User not found") }

        if (!passwordEncoder.matches(body.password, user.password))
            throw StatusCodeException(401, "Password is incorrect")

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(body.login, body.password)
        )

        val jwtToken = jwtService.generateToken(user)

        revokeUserTokens(user)
        saveUserToken(user, jwtToken)

        return SignInResponse(jwtToken)
    }

    fun register(body: RegisterRequest): Any {
        if (userRepository.existsByLogin(body.login) ||
            userRepository.existsByEmail(body.email) ||
            (body.phone != null && userRepository.existsByPhone(body.phone))) {
            throw StatusCodeException(409, "User with this registration information already exists")
        }

        if (body.login.length > 30) throw StatusCodeException(400, "Login is too long")
        if (!body.login.matches(Regex("^[a-zA-Z0-9-]+\$"))) throw StatusCodeException(400, "Login must match [a-zA-Z0-9-]+")

        if (body.email.length > 50) throw StatusCodeException(400, "Email is too long")

        if (body.password.length < 6 || body.password.length > 100) throw StatusCodeException(400, "Password must be between 6 and 100 characters")

        if (body.countryCode.length != 2) throw StatusCodeException(400, "Country code must be 2 characters long")
        if (!body.countryCode.matches(Regex("^[a-zA-Z]{2}\$"))) throw StatusCodeException(400, "Country code must match [a-zA-Z]{2}")

        if (body.phone != null && !body.phone.matches(Regex("^\\+\\d+\$"))) throw StatusCodeException(400, "Phone must match ^\\+\\d+\$")
        if (body.image != null && body.image.length > 200) throw StatusCodeException(400, "Image URL is too long")

        val user = User(
            0,
            body.login,
            body.email,
            passwordEncoder.encode(body.password),
            countryRepository.findByAlpha2(body.countryCode)
                .orElseThrow { StatusCodeException(400, "Invalid country code") },
            body.isPublic,
            body.phone,
            body.image,
        )

        val savedUser = userRepository.save(user)

        return RegisterResponse(
            savedUser.toMap()
        )
    }

    fun revokeUserTokens(user: User) {
        val validTokens = tokenRepository.findAllValidTokensByUserId(user.id)
        if (validTokens.isNotEmpty()) {
            validTokens.forEach { it.revoked = true }
            tokenRepository.saveAll(validTokens)
        }
    }

    fun saveUserToken(user: User, token: String) {
        val newToken = Token(0, token, TokenType.BEARER, revoked = false, expired = false, user = user)
        tokenRepository.save(newToken)
    }

}