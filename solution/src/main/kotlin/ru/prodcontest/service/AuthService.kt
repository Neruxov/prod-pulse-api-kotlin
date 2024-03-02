package ru.prodcontest.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.prodcontest.data.country.repo.CountryRepository
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.repo.UserRepository
import ru.prodcontest.data.user.request.RegisterRequest
import ru.prodcontest.data.user.request.SignInRequest
import ru.prodcontest.data.user.response.RegisterResponse
import ru.prodcontest.data.user.response.SignInResponse
import ru.prodcontest.exception.type.StatusCodeException
import ru.prodcontest.util.PasswordUtil

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class AuthService(
    val userRepository: UserRepository,
    val jwtService: JwtService,
    val countryRepository: CountryRepository,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager
) {

    fun signIn(body: SignInRequest): Any {
        if (body.login == "my")
            throw StatusCodeException(400, "Login 'my' is not allowed")

        if (body.login.length > 30 || body.login.isEmpty())
            throw StatusCodeException(400, "Login must be less more than 30 characters long and not be empty")

        val auth: Authentication
        try {
            auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(body.login, body.password)
            )
        } catch (e: AuthenticationException) {
            throw StatusCodeException(401, "Password is incorrect")
        }

        val jwtToken = jwtService.generateToken(auth.principal as User)
        return SignInResponse(jwtToken)
    }

    fun register(body: RegisterRequest): Any {
        if (body.login.length > 30)
            throw StatusCodeException(400, "Login must be less more than 30 characters long and not be empty")

        if (!body.login.matches(Regex("^[a-zA-Z0-9-]+\$")))
            throw StatusCodeException(400, "Login must match [a-zA-Z0-9-]+")

        if (body.email.length > 50 || body.email.isEmpty())
            throw StatusCodeException(400, "Email must be less than 50 characters long and not be empty")

        if (!PasswordUtil.meetsRequirements(body.password))
            throw StatusCodeException(
                400,
                "Password must be between 6 and 100 characters, contain at least one digit, one lowercase letter, and one uppercase letter."
            )

        if (!body.countryCode.matches(Regex("^[a-zA-Z]{2}\$")))
            throw StatusCodeException(400, "Country code must match [a-zA-Z]{2}")

        if (body.phone != null && (!body.phone.matches(Regex("^\\+\\d+")) || body.phone.length > 20))
            throw StatusCodeException(
                400,
                "Phone must match ^\\+\\d+ and be less than 20 characters long"
            )

        if (body.image != null && (body.image.length > 200 || body.image.isEmpty()))
            throw StatusCodeException(400, "Image URL is too long or empty")

        if (userRepository.existsByLoginOrPhoneOrEmail(
            body.login,
            body.phone,
            body.email
        )) {
            throw StatusCodeException(409, "User with this registration information already exists")
        }

        if (!countryRepository.existsByAlpha2(body.countryCode))
            throw StatusCodeException(400, "Invalid country code")

        val user = User(
            body.login,
            body.email,
            passwordEncoder.encode(body.password),
            body.countryCode,
            body.isPublic,
            body.phone,
            body.image,
        )

        val savedUser = userRepository.save(user)

        return RegisterResponse(
            savedUser.toMap()
        )
    }

}