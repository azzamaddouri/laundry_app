package com.uptech.buanderie.security

import com.uptech.buanderie.Log
import com.uptech.buanderie.models.entity.User
import io.jsonwebtoken.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

@Component
object SecurityUtils {
    const val JWT_HEADER_NAME = "Authorization"
    const val SECRET = "ds(p~Y5Y%UR;g^pv"
    const val SESSION_EXPIRATION = 1000L * 3600 * 24 * 365
    const val RESET_PWD_TOKEN_EXPIRATION = 1000 * 60 * 30
    const val SET_PWD_TOKEN_EXPIRATION = 1000 * 3600 * 24 * 5
    const val HEADER_PREFIX = "Bearer "

    val AUTH_WHITELIST = arrayOf(
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/**",
        "/swagger-ui.html/**",
        "/webjars/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/api/v1/auth/**",
    )

    fun build(user: User): UserDetailsImpl {
        val authorities: List<GrantedAuthority> = user.roleList.stream()
            .map { role -> SimpleGrantedAuthority(role.libelle) }
            .collect(Collectors.toList())
        return UserDetailsImpl(
            user.id,
            user.email,
            user.pwd,
            authorities
        )
    }

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl
        val jwtExpirationMs: Long = SESSION_EXPIRATION
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .setIssuer(userPrincipal.authorities.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact()
    }

    fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7)
        } else null
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).body.subject
    }

    fun getUserIssuerFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).body.issuer
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            Log.e("Invalid JWT signature: {}", e.message.toString())
        } catch (e: MalformedJwtException) {
           Log.e("Invalid JWT token: {}", e.message.toString())
        } catch (e: ExpiredJwtException) {
           Log.e("JWT token is expired: {}", e.message.toString())
        } catch (e: UnsupportedJwtException) {
           Log.e("JWT token is unsupported: {}", e.message.toString())
        } catch (e: IllegalArgumentException) {
           Log.e("JWT claims string is empty: {}", e.message.toString())
        }
        return false
    }
}