package de.pydir.security

import de.pydir.repository.AccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val accountRepository: AccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = accountRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("User not found") }

        return User(
            user.email,
            user.password,
            emptyList()
        )
    }
}
