package ru.prodcontest.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.request.UpdateRequest
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