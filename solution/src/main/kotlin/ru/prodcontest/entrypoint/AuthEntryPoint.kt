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
        if (response?.status == 403) {
            response.status = 401
            response.contentType = "application/json"
            response.writer?.write("{\"reason\": \"Invalid credentials\"}")
        } else if (response?.status == 400) {
            response.status = 400
            response.contentType = "application/json"
            response.writer?.write("{\"reason\": \"Invalid body\"}")
        }
    }

}