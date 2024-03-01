package ru.prodcontest.data.token.model

import jakarta.persistence.*
import ru.prodcontest.data.token.enums.TokenType
import ru.prodcontest.data.user.model.User

@Entity
@Table(name = "tokens")
data class Token(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    val id: Long,

    @Column(name = "token", unique = true)
    val token: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: TokenType,

    @Column(name = "revoked")
    var revoked: Boolean,

    @Column(name = "user_id")
    val userId: Long

) {

    constructor() : this(0, "", TokenType.BEARER, false, 0)

}
