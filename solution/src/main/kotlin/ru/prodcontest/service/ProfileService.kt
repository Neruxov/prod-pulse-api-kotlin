package ru.prodcontest.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import ru.prodcontest.data.user.model.User

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Service
class ProfileService(

) {

    fun getMyProfile(userDetails: UserDetails): Any {
        val user = userDetails as User
        return user.toMap()
    }

}