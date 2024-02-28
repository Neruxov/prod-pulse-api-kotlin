package ru.prodcontest.data.post.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.post.model.Post
import java.util.UUID

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface PostRepository : JpaRepository<Post, UUID> {
}