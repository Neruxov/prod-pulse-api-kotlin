package ru.prodcontest.data.friend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.prodcontest.data.user.model.User
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

    @Column(name = "user_login")
    val userLogin: String,

    @Column(name = "friend_login")
    val friendLogin: String,

    @Column(name = "added_at")
    val addedAt: Date = Date()

) {

    constructor() : this(0, "", "")

}