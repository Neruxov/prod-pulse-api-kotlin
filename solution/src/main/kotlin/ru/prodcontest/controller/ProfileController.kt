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
@RequestMapping("/api")
class ProfileController(
    val profileService: ProfileService
) {

    @GetMapping("/me/profile")
    fun getMyProfile(@AuthenticationPrincipal user: User) = user.toMap()

    @PatchMapping("/me/profile")
    fun updateMyProfile(
        @AuthenticationPrincipal user: User,
        @RequestBody request: UpdateRequest
    ) = profileService.updateMyProfile(user, request)

    @GetMapping("/profiles/{login}")
    fun getProfile(
        @AuthenticationPrincipal user: User,
        @PathVariable login: String
    ) = profileService.getProfile(user, login)

}