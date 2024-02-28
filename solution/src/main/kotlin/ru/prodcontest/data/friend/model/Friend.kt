package ru.prodcontest.data.friend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.prodcontest.data.user.model.User
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Date

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Entity
@Table(name = "friends")
data class Friend(

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    val friend: User,

    @Column(name = "added_at")
    val addedAt: Date = Date()

) {

    constructor() : this(0, User(), User())

}