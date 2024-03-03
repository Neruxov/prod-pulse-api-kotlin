package ru.prodcontest.util.entrypoint

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
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
        if (response == null) return

        val reason = when (response.status) {
            403 -> "Invalid credentials"
            400 -> "Invalid body"
            404 -> "Endpoint not found"
            405 -> "Method not allowed"
            415 -> "Unsupported media type"

            else -> return
        }

        response.contentType = "application/json"
        response.writer?.write("{\"reason\": \"$reason\"}")
    }

}