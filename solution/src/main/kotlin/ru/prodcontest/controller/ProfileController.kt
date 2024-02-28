package ru.prodcontest.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    fun getMyProfile(@AuthenticationPrincipal userDetails: UserDetails) = profileService.getMyProfile(userDetails)

}