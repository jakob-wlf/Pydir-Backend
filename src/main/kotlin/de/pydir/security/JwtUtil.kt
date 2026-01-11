package de.pydir.security

import de.pydir.entity.Account
import de.pydir.repository.AccountRepository
import io.jsonwebtoken.Claims
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
    private val expirationMs: Long,

    private val accountRepository: AccountRepository
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(account: Account): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        val roles = account.roles.joinToString(",") { it.name }

        return Jwts.builder()
            .setSubject(account.email)
            .claim("accountId", account.id.toString())
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

    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun getAccountIdFromToken(token: String): String {
        return getClaims(token)["accountId"] as String
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = getClaims(token)
            val accountId = claims["accountId"] as? String

            // Verify the account still exists and matches
            if (accountId != null) {
                val account = accountRepository.findById(accountId.toLong())
                    .orElse(null)

                // Token is invalid if account doesn't exist or email doesn't match
                account != null && account.email == claims.subject
            } else {
                false
            }
        } catch (ex: Exception) {
            false
        }
    }
}

