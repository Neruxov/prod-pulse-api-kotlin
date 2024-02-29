package ru.prodcontest.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashMap


/**
 * @author <a href="https://github.com/ali-bouali/spring-boot-3-jwt-security/blob/main/src/main/java/com/alibou/security/config/JwtService.java">ali-bouali</a>
 */
@Service
class JwtService {

    private val secretKey: String = "245c6b5f623630436d5b70693c5f4d5a5c46673850292e257571502657"
    private val jwtExpiration: Long = 60 * 60

    fun extractUsername(token: String?): String? {
        return extractClaim<String>(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String?, claimsResolver: (Claims) -> T): T? {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }

    fun generateToken(
        extraClaims: Map<String?, Any?>,
        userDetails: UserDetails
    ): String {
        val nonce = UUID.randomUUID().toString()
        val updatedClaims = HashMap(extraClaims)
        updatedClaims["nonce"] = nonce

        return buildToken(updatedClaims, userDetails, jwtExpiration)
    }

    private fun buildToken(
        extraClaims: Map<String?, Any?>,
        userDetails: UserDetails,
        expiration: Long
    ): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token)?.before(Date()) ?: true
    }

    private fun extractExpiration(token: String?): Date? {
        return extractClaim<Date>(token, Claims::getExpiration)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}