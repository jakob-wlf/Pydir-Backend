package de.pydir.service

import de.pydir.data.AccountProperties
import de.pydir.dto.ChangePasswordRequest
import de.pydir.dto.LoginRequest
import de.pydir.dto.RegisterRequest
import de.pydir.entity.Account
import de.pydir.repository.RoleRepository
import de.pydir.repository.AccountRepository
import de.pydir.security.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val accountRepository: AccountRepository,
    private val roleRepository: RoleRepository,
    private val accountProperties: AccountProperties,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    //TODO: Validate that email, username, phone number and password meet requirements
    fun register(request: RegisterRequest): String {
        if (accountRepository.findByEmail(request.email).isPresent) {
            throw IllegalArgumentException("Email already in use")
        }

        val account = Account(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            username = request.username,
            phoneNumber = request.phoneNumber,

            rockets = accountProperties.startingRocketAmount,
            coins = accountProperties.startingCoinAmount,
            credits = accountProperties.startingCreditAmount
        )

        val userRole = roleRepository.findByName("ROLE_USER")
            .orElseThrow { IllegalStateException("ROLE_USER not found") }

        account.roles.add(userRole)

        accountRepository.save(account)

        return jwtUtil.generateToken(account)
    }

    fun login(request: LoginRequest): String {
        val account = accountRepository.findByUsername(request.username)
            .orElseThrow { IllegalArgumentException("Invalid username or password") }

        if (!passwordEncoder.matches(request.password, account.password)) {
            throw IllegalArgumentException("Invalid username or password")
        }

        return jwtUtil.generateToken(account)
    }
}