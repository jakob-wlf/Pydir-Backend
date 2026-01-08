package de.pydir.security

import de.pydir.entity.Account
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.expiration}")
    private val expirationMs: Long
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(account: Account): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        val roles = account.roles.joinToString(",") { it.name }

        return Jwts.builder()
            .setSubject(account.email)
            .claim("roles", roles)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getEmailFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun getRolesFromToken(token: String): List<String> {
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

        val rolesString = claims["roles"] as? String ?: ""
        return if (rolesString.isNotEmpty()) {
            rolesString.split(",")
        } else {
            emptyList()
        }
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (ex: Exception) {
            false
        }
    }
}

