package ru.prodcontest.data.friend.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.prodcontest.data.friend.model.Friend

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface FriendRepository : JpaRepository<Friend, Long> {

    fun findByUserLogin(login: String): List<Friend>

}