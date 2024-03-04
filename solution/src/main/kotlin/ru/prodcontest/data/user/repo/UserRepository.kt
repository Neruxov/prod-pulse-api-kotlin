package ru.prodcontest.data.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.user.model.User
import java.util.*

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface UserRepository : JpaRepository<User, Long> {

    fun findByLogin(login: String): Optional<User>

    fun existsByLoginOrEmail(login: String, email: String): Boolean

    fun existsByPhone(phone: String): Boolean

}