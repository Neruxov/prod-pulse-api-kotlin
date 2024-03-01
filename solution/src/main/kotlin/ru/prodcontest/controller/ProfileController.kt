package ru.prodcontest.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.prodcontest.data.user.model.User
import ru.prodcontest.service.ProfileService

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@RestController
@RequestMapping("/api/profiles")
class ProfileController(
    val profileService: ProfileService
) {

    @GetMapping("/{login}")
    fun getProfile(
        @AuthenticationPrincipal user: User,
        @PathVariable login: String
    ) = profileService.getProfile(user, login)

}