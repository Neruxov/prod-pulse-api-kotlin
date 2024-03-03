package ru.prodcontest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.prodcontest.util.entrypoint.AuthEntryPoint
import ru.prodcontest.filter.JwtAuthenticationFilter

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    val authenticationProvider: AuthenticationProvider,
    val jwtAuthenticationFilter: JwtAuthenticationFilter,
    val authenticationEntryPoint: AuthEntryPoint
) {

    companion object {

        val WHITELISTED_URLS = arrayOf(
            "/api/auth/**",
            "/api/ping/**",
            "/api/countries/**"
        )

    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests {
                WHITELISTED_URLS.forEach { url -> it.requestMatchers(url).permitAll() }
                it.anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { it.authenticationEntryPoint(authenticationEntryPoint) }
        return http.build()
    }

}