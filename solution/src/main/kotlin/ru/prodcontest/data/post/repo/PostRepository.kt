package ru.prodcontest.data.post.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.prodcontest.data.post.model.Post
import java.util.*

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface PostRepository : JpaRepository<Post, UUID> {

    @Query(
        "select p from Post p " +
                "where p.user.login = :login " +
        "order by p.createdAt desc " +
        "limit :limit offset :offset"
    )
    fun findByUserLoginOrderByCreatedAtDescPaged(
        login: String,
        limit: Int,
        offset: Int
    ): List<Post>

}