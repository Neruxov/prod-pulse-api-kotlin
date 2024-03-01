package ru.prodcontest.data.post.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.post.enums.ReactionType
import ru.prodcontest.data.post.model.Reaction
import java.util.Optional
import java.util.UUID

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface ReactionRepository : JpaRepository<Reaction, Long> {

    fun findByPostIdAndUserId(postId: UUID, userId: Long): Optional<Reaction>

    fun countByPostIdAndType(postId: UUID, type: ReactionType): Int

}