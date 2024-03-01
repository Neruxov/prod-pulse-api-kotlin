package ru.prodcontest.data.post.model

import jakarta.persistence.*
import ru.prodcontest.data.post.enums.ReactionType
import java.util.UUID

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Entity
@Table(name = "reactions")
data class Reaction(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Long,

    @Column(name = "post_id")
    val postId: UUID,

    @Column(name = "user_id")
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var type: ReactionType

) {

    constructor() : this(0, UUID.randomUUID(), 0, ReactionType.LIKE)

}