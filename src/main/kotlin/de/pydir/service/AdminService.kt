package de.pydir.service

import de.pydir.dto.DetailedAccountResponse
import de.pydir.repository.RoleRepository
import de.pydir.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminService(
    private val accountRepository: AccountRepository,
    private val roleRepository: RoleRepository
) {

    @Transactional
    fun grantAdminRole(email: String) {
        val account = accountRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }
        
        val adminRole = roleRepository.findByName("ROLE_ADMIN")
            .orElseThrow { IllegalStateException("ROLE_ADMIN not found") }
        
        account.roles.add(adminRole)
        accountRepository.save(account)
    }
    
    @Transactional
    fun revokeAdminRole(email: String) {
        val account = accountRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }
        
        account.roles.removeIf { it.name == "ROLE_ADMIN" }
        accountRepository.save(account)
    }

    fun getAccountDetails(accountId: Long): DetailedAccountResponse? {
        val account = accountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account not found") }

        return DetailedAccountResponse.fromAccount(account)
    }

    fun getAccountIdByUsername(username: String): Long {
        println("Looking up account ID for username: $username")
        val account = accountRepository.findByUsername(username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        return account.id
    }
}