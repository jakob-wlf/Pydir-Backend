package de.pydir.service

import de.pydir.dto.DetailedAccountResponse
import de.pydir.repository.RoleRepository
import de.pydir.repository.AccountRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
            .orElseThrow { IllegalArgumentException("Account not found") }

        account.roles.removeIf { it.name == "ROLE_ADMIN" }
        accountRepository.save(account)
    }

    fun getAccountDetails(accountId: Long): DetailedAccountResponse? {
        val account = accountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account not found") }

        return DetailedAccountResponse.fromAccount(account)
    }

    fun getAccountIdByUsername(username: String): Long {
        val account = accountRepository.findByUsername(username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        return account.id
    }

    fun getPaginatedAccounts(pageable: Pageable, includeBots: Boolean): Page<DetailedAccountResponse> {
        return if(includeBots) accountRepository.findAll(pageable).map { DetailedAccountResponse.fromAccount(it) }
        else accountRepository.findByIsBotFalse(pageable).map { DetailedAccountResponse.fromAccount(it) }
    }

    fun deleteAccount(accountId: Long) {
        val account = accountRepository.findById(accountId)
            .orElseThrow { IllegalArgumentException("Account not found") }

        accountRepository.delete(account)
    }

    fun deleteAllAccounts() {
        accountRepository.deleteAll()
    }

    @Value("\${application.client-version}")
    private val clientVersion: String = ""

    fun getClientVersion(): String {
        return clientVersion
    }
}