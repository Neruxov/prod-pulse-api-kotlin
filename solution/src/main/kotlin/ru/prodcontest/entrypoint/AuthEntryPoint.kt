package ru.prodcontest.entrypoint

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Component
class AuthEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.status = 401
        response?.contentType = "application/json"
        response?.writer?.write("{\"reason\": \"Invalid credentials\"}")
    }

}