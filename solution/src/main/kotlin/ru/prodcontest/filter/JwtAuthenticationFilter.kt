package ru.prodcontest.filter

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.prodcontest.config.SecurityConfig
import ru.prodcontest.data.user.repo.UserRepository
import ru.prodcontest.service.JwtService
import java.io.IOException
import java.util.Date
import kotlin.jvm.optionals.getOrElse

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Component
class JwtAuthenticationFilter(
    val jwtService: JwtService,
    val userDetailsService: UserDetailsService,
    val userRepository: UserRepository
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (SecurityConfig.WHITELISTED_URLS.any { request.requestURI.startsWith(it.substringBefore("/**")) })
            return filterChain.doFilter(request, response)

        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.status = 401
            response.contentType = "application/json"
            response.writer.write("{\"reason\": \"Token not found\"}")
            return
        }

        val jwt = authHeader.substring(7)

        val login: String?
        val issuedAt: Date?

        try {
            login = jwtService.extractUsername(jwt)
            issuedAt = jwtService.extractClaim(jwt) { claim -> return@extractClaim claim.issuedAt }
        } catch (e: ExpiredJwtException) {
            response.status = 401
            response.contentType = "application/json"
            response.writer.write("{\"reason\": \"Token expired\"}")
            return
        } catch (e: Exception) {
            response.status = 401
            response.contentType = "application/json"
            response.writer.write("{\"reason\": \"Invalid token\"}")
            return
        }

        if (login != null && issuedAt != null && SecurityContextHolder.getContext().authentication == null) {
            val user = userRepository.findByLogin(login)
                .getOrElse {
                    response.status = 401
                    response.contentType = "application/json"
                    response.writer.write("{\"reason\": \"Invalid token\"}")
                    return
                }

            val isTokenValid = issuedAt.after(user.lastPasswordChange)

            if (jwtService.isTokenValid(jwt, user) && isTokenValid) {
                val authToken = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            } else {
                response.status = 401
                response.contentType = "application/json"
                response.writer.write("{\"reason\": \"Invalid token\"}")
                return
            }
        }

        filterChain.doFilter(request, response)
    }

}