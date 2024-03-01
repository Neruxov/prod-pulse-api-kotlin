package ru.prodcontest.data.post.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "tags")
data class Tag(

    @Id
    @JsonIgnore
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @Column(name = "name")
    val name: String,

    @Column(name = "post_id")
    val postId: UUID

) {

    constructor() : this(0, "", UUID.randomUUID())

}
