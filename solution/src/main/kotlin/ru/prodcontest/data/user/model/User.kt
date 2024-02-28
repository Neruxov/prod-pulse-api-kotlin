package ru.prodcontest.data.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.prodcontest.data.countries.model.Country
import ru.prodcontest.data.user.enums.Role

@Entity
@Table(name = "users")
data class User(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,

    @Column(name = "login")
    val login: String,

    @Column(name = "email")
    val email: String,

    @JsonIgnore
    @Column(name = "password")
    val _password: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code", referencedColumnName = "alpha2")
    val country: Country,

    @Column(name = "is_public")
    val isPublic: Boolean,

    @Column(name = "phone", nullable = true)
    val phone: String?,

    @Column(name = "image", nullable = true)
    val image: String?,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER

) : UserDetails {

    constructor() : this(0, "", "", "", Country(), false, "", "")

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