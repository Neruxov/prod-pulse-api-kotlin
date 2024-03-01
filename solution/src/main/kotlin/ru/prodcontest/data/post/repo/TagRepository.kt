package ru.prodcontest.data.post.repo

import org.springframework.data.jpa.repository.JpaRepository
import ru.prodcontest.data.post.model.Tag

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
interface TagRepository : JpaRepository<Tag, Long>