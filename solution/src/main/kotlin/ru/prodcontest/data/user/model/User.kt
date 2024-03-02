package ru.prodcontest.data.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.prodcontest.data.user.enums.Role
import java.util.Date

@Entity
@Table(name = "users")
data class User(

    @Id
    @Column(name = "login")
    val login: String,

    @Column(name = "email")
    val email: String,

    @JsonIgnore
    @Column(name = "password")
    var _password: String,

    @Column(name = "country_code")
    var countryCode: String,

    @Column(name = "is_public")
    var isPublic: Boolean,

    @Column(name = "phone", nullable = true)
    var phone: String?,

    @Column(name = "image", nullable = true)
    var image: String?,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER,

    @Column(name = "last_password_change")
    var lastPasswordChange: Date = Date(),

    ) : UserDetails {

    constructor() : this("", "", "", "", false, "", "")

    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "login" to this.login,
            "email" to this.email,
            "countryCode" to this.countryCode,
            "isPublic" to this.isPublic
        )

        if (this.phone != null) map["phone"] = this.phone!!
        if (this.image != null) map["image"] = this.image!!

        return map
    }

    override fun getPassword(): String {
        return _password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getUsername(): String {
        return login
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}