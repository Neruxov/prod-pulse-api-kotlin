package ru.prodcontest.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.prodcontest.data.country.repo.CountryRepository
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.repo.UserRepository
import ru.prodcontest.data.user.request.PasswordUpdateRequest
import ru.prodcontest.data.user.request.UpdateRequest
import ru.prodcontest.exception.type.StatusCodeException
import ru.prodcontest.util.PasswordUtil
import java.util.*

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
                if (request.countryCode == user.countryCode) return@let

                if (!it.matches(Regex("[a-zA-Z]{2}")))
                    throw StatusCodeException(400, "Alpha2 must match [a-zA-Z]{2}.")

                if (!countryRepository.existsByAlpha2(it))
                    throw StatusCodeException(400, "Country with this alpha2 does not exist")

                countryCode = it
            }

            request.isPublic?.let {
                if (user.isPublic == it) return@let

                isPublic = it
            }

            request.phone?.let {
                if (request.phone == user.phone) return@let

                if (!it.matches(Regex("^\\+\\d+")) || it.length > 20)
                    throw StatusCodeException(
                        400,
                        "Phone must match ^\\+\\d+ and be less than 20 characters long"
                    )

                if (userRepository.existsByPhone(it))
                    throw StatusCodeException(409, "User with this phone already exists")

                phone = it
            }

            request.image?.let {
                if (request.image == user.image) return@let

                if (it.length > 200 || it.isEmpty())
                    throw StatusCodeException(400, "Image URL is too long or empty")

                image = it
            }
        }

        userRepository.save(user)
        return user.toMap()
    }

    fun updatePassword(user: User, body: PasswordUpdateRequest): Any {
        if (body.newPassword.isEmpty() || body.oldPassword.isEmpty())
            throw StatusCodeException(400, "Passwords must not be empty")

        if (!PasswordUtil.meetsRequirements(body.newPassword))
            throw StatusCodeException(
                400,
                "Password must be between 6 and 100 characters, contain at least one digit, one lowercase letter, and one uppercase letter."
            )

        if (!passwordEncoder.matches(body.oldPassword, user.password))
            throw StatusCodeException(403, "Old password is incorrect")

        user._password = passwordEncoder.encode(body.newPassword)
        user.lastPasswordChange = Date()

        userRepository.save(user)
        return mapOf(
            "status" to "ok"
        )
    }

}