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
import ru.prodcontest.data.token.repo.TokenRepository
import ru.prodcontest.service.JwtService
import java.io.IOException

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Component
class JwtAuthenticationFilter(
    val jwtService: JwtService,
    val userDetailsService: UserDetailsService,
    val tokenRepository: TokenRepository
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
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return filterChain.doFilter(request, response)

        val jwt = authHeader.substring(7)

        val login: String?
        try {
            login = jwtService.extractUsername(jwt)
        } catch (e: ExpiredJwtException) {
            println(e)
            response.status = 401
            response.contentType = "application/json"
            response.writer.write("{\"reason\": \"Token expired\"}")
            return
        }

        if (login != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(login)
            val isTokenValid = tokenRepository.findByToken(jwt)
                .map { t -> !t.revoked }
                .orElse(false)

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
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