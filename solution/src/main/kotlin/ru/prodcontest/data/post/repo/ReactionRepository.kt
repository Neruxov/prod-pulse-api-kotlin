package ru.prodcontest.data.post.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.post.model.Reaction

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface ReactionRepository : JpaRepository<Reaction, Long> {
}