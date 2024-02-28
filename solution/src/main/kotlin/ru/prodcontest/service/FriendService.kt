package ru.prodcontest.service

import org.springframework.stereotype.Service
import ru.prodcontest.data.friend.model.Friend
import ru.prodcontest.data.friend.repo.FriendRepository
import ru.prodcontest.data.friend.request.FriendRequest
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.repo.UserRepository
import ru.prodcontest.exception.type.StatusCodeException
import ru.prodcontest.util.DateFormatter

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class FriendService(
    val friendRepository: FriendRepository,
    val userRepository: UserRepository
) {

    fun addFriend(user: User, body: FriendRequest): Any {
        val userFriends = friendRepository.findByUserLoginOrderByAddedAtDesc(user.login)
        if (user.login == body.login || userFriends.any { it.friend.login == body.login }) {
            return mapOf("status" to "ok")
        }

        val friendUser = userRepository.findByLogin(body.login)
            .orElseThrow { StatusCodeException(404, "User not found") }

        val friend = Friend(0, user, friendUser)

        friendRepository.save(friend)
        return mapOf("status" to "ok")
    }

    fun removeFriend(user: User, body: FriendRequest): Any {
        val userFriends = friendRepository.findByUserLoginOrderByAddedAtDesc(user.login)
        val friend = userFriends.find { it.friend.login == body.login }
            ?: return mapOf("status" to "ok")

        friendRepository.delete(friend)
        return mapOf("status" to "ok")
    }

    fun getFriends(user: User, limit: Int, offset: Int): Any {
        if (limit < 0 || limit > 50)
            throw StatusCodeException(400, "Limit must be between 0 and 50")

        if (offset < 0)
            throw StatusCodeException(400, "Offset must be greater than 0")

        val userFriends = friendRepository.findByUserLoginOrderByAddedAtDesc(user.login)
        if (offset >= userFriends.size)
            return emptyList<Map<String, Any>>()

        val friends = userFriends.subList(offset, offset + limit.coerceAtMost(userFriends.size))
        return friends.map { mapOf(
            "login" to it.friend.login,
            "addedAt" to DateFormatter.format(it.addedAt)
        ) }
    }

}