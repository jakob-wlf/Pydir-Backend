package de.pydir.service

import de.pydir.dto.AccountResponse
import de.pydir.entity.Account
import de.pydir.repository.AccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
) {

    fun getAccountDetails(user: User): AccountResponse {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { IllegalArgumentException("Invalid username or password") }

        return AccountResponse.fromAccount(account)
    }
}