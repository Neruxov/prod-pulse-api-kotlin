package ru.prodcontest.data.friend.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.friend.model.Friend
import java.util.*

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface FriendRepository : JpaRepository<Friend, Long> {

    fun findByUserLoginOrderByAddedAtDesc(login: String): List<Friend>

    fun existsByUserLoginAndFriendLogin(userLogin: String, friendLogin: String): Boolean
    fun findByUserLoginAndFriendLogin(userLogin: String, friendLogin: String): Optional<Friend>

}