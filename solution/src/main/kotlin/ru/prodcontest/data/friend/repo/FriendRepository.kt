package ru.prodcontest.data.friend.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.friend.model.Friend

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface FriendRepository : JpaRepository<Friend, Long> {

    fun findByUserLoginOrderByAddedAt(login: String): List<Friend>
    fun existsByUserLoginAndFriendLogin(userLogin: String, friendLogin: String): Boolean

}