package ru.prodcontest.data.post.model

import jakarta.persistence.*
import ru.prodcontest.data.user.model.User
import ru.prodcontest.util.DateFormatter
import java.util.*

@Entity
@Table(name = "posts")
data class Post(

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID")
    val id: UUID,

    @Column(name = "content", length = 1000)
    val content: String,

    @Column(name = "tags")
    val tags: Array<String>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_login", referencedColumnName = "login")
    val user: User,

    @Column(name = "created_at")
    val createdAt: Date = Date(),

    @Column(name = "likesCount")
    var likesCount: Int = 0,

    @Column(name = "dislikesCount")
    var dislikesCount: Int = 0

) {

    constructor() : this(UUID.randomUUID(), "", arrayOf(), User(), Date())

    fun toMap() = mapOf(
        "id" to this.id,
        "content" to this.content,
        "author" to this.user.login,
        "tags" to this.tags.map { it },
        "createdAt" to DateFormatter.format(this.createdAt),
        "likesCount" to likesCount,
        "dislikesCount" to dislikesCount
    )

}
