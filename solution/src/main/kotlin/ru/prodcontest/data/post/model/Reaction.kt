package ru.prodcontest.data.post.model

import jakarta.persistence.*
import ru.prodcontest.data.post.enums.ReactionType
import ru.prodcontest.data.user.model.User

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: ReactionType

) {

    constructor() : this(0, Post(), User(), ReactionType.LIKE)

}