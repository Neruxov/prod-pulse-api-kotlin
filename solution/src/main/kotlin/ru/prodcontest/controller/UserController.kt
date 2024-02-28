package ru.prodcontest.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.prodcontest.data.user.model.User
import ru.prodcontest.data.user.request.PasswordUpdateRequest
import ru.prodcontest.data.user.request.UpdateRequest
import ru.prodcontest.service.UserService

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@RestController
@RequestMapping("/api/me")
class UserController(
    val userService: UserService
) {

    @GetMapping("/profile")
    fun getProfile(@AuthenticationPrincipal user: User) = user.toMap()

    @PatchMapping("/profile")
    fun updateMyProfile(
        @AuthenticationPrincipal user: User,
        @RequestBody request: UpdateRequest
    ) = userService.updateMyProfile(user, request)

    @PostMapping("/updatePassword")
    fun updatePassword(
        @AuthenticationPrincipal user: User,
        @RequestBody body: PasswordUpdateRequest
    ) = userService.updatePassword(user, body)

}