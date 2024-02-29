package ru.prodcontest.data.token.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.prodcontest.data.token.model.Token
import java.util.*

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface TokenRepository : JpaRepository<Token, Long> {

    fun findByToken(token: String): Optional<Token>

    fun findByUserIdAndRevokedFalse(userId: Long): List<Token>

}