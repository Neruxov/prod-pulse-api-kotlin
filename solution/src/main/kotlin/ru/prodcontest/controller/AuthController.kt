package ru.prodcontest.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.prodcontest.data.user.request.RegisterRequest
import ru.prodcontest.data.user.request.SignInRequest
import ru.prodcontest.service.AuthService

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    val authService: AuthService
) {

    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody body: SignInRequest
    ) = authService.signIn(body)

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @RequestBody request: RegisterRequest
    ) = authService.register(request)

}