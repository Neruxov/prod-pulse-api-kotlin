package ru.prodcontest.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.prodcontest.data.countries.repo.CountryRepository
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.repo.UserRepository
import ru.prodcontest.data.user.request.UpdateRequest
import ru.prodcontest.exception.type.StatusCodeException

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class ProfileService(
    val userRepository: UserRepository
) {

    fun getProfile(user: User, login: String): Any {
        if (login == user.login)
            return user.toMap()

        val profile = userRepository.findByLogin(login)
            .orElseThrow { StatusCodeException(403, "User not found") }

        if (!profile.isPublic)
            throw StatusCodeException(403, "User is private")

        return profile.toMap()
    }

}