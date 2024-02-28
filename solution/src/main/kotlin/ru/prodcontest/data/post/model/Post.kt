package ru.prodcontest.data.post.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
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

    @OneToMany(mappedBy = "post")
    val tags: List<Tag>,

    @Column(name = "created_at")
    val createdAt: Date = Date(),

    @Column(name = "likes_count")
    var likesCount: Int = 0,

    @Column(name = "dislikes_count")
    var dislikesCount: Int = 0

) {

    constructor() : this(UUID.randomUUID(), "", emptyList())

}
