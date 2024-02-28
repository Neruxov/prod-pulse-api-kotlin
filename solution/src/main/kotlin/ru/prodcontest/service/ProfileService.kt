package ru.prodcontest.service

import org.springframework.stereotype.Service
import ru.prodcontest.data.friend.repo.FriendRepository
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.repo.UserRepository
import ru.prodcontest.exception.type.StatusCodeException

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class ProfileService(
    val userRepository: UserRepository,
    val friendsRepository: FriendRepository
) {

    fun getProfile(user: User, login: String): Any {
        if (login == user.login)
            return user.toMap()

        val profile = userRepository.findByLogin(login)
            .orElseThrow { StatusCodeException(403, "User not found") }

        if (!profile.isPublic && !friendsRepository.existsByUserLoginAndFriendLogin(login, user.login))
            throw StatusCodeException(403, "User is private")

        return profile.toMap()
    }

}