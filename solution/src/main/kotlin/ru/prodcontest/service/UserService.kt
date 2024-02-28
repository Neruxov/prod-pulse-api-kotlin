package ru.prodcontest.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.prodcontest.data.country.repo.CountryRepository
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.repo.UserRepository
import ru.prodcontest.data.user.request.PasswordUpdateRequest
import ru.prodcontest.data.user.request.UpdateRequest
import ru.prodcontest.exception.type.StatusCodeException

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class UserService(
    val userRepository: UserRepository,
    val countryRepository: CountryRepository,
    val passwordEncoder: PasswordEncoder,
    val authService: AuthService
) {

    fun updateMyProfile(user: User, request: UpdateRequest): Any {
        user.apply {
            request.countryCode?.let {
                val countryNew = countryRepository.findByAlpha2OrderByAlpha2(it).orElseThrow { StatusCodeException(404, "Country not found") }
                country = countryNew
            }

            request.isPublic?.let { isPublic = it }

            request.phone?.let {
                if (!it.matches(Regex("^\\+\\d+"))) throw StatusCodeException(400, "Phone must match ^\\+\\d+")
                phone = it
            }

            request.image?.let {
                if (it.length > 200) throw StatusCodeException(400, "Image URL is too long")
                image = it
            }
        }

        userRepository.save(user)
        return user.toMap()
    }

    fun updatePassword(user: User, body: PasswordUpdateRequest): Any {
        if (body.newPassword.length < 6 || body.newPassword.length > 100) throw StatusCodeException(400, "Password must be between 6 and 100 characters")

        if (!passwordEncoder.matches(body.oldPassword, user.password))
            throw StatusCodeException(403, "Old password is incorrect")

        authService.revokeUserTokens(user)
        user._password = passwordEncoder.encode(body.newPassword)

        userRepository.save(user)
        return mapOf(
            "status" to "ok"
        )
    }

}