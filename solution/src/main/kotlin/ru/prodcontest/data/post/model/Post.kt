package ru.prodcontest.data.post.model

import jakarta.persistence.*
import ru.prodcontest.data.user.model.User
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "posts")
data class Post(

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    val id: UUID,

    @Column(name = "content")
    val content: String,

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    val tags: List<Tag>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    @Column(name = "created_at")
    val createdAt: Date = Date(),

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    val reactions: List<Reaction> = emptyList()

) {

    constructor() : this(UUID.randomUUID(), "", emptyList(), User(), Date(), emptyList())

}
