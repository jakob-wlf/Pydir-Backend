package de.pydir.security

import de.pydir.repository.AccountRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService,
    private val accountRepository: AccountRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            if (jwtUtil.validateToken(token)) {
                val email = jwtUtil.getEmailFromToken(token)
                val accountId = jwtUtil.getAccountIdFromToken(token)

                // Verify account exists and matches
                val account = accountRepository.findById(accountId.toLong())
                    .orElse(null)

                if (account != null && account.email == email) {
                    // Use CURRENT roles from database, not JWT
                    val authorities = account.roles.map {
                        SimpleGrantedAuthority(it.name)
                    }

                    val userDetails: UserDetails = userDetailsService.loadUserByUsername(email)
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                    )
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}