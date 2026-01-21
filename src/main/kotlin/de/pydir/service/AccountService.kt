package de.pydir.service

import de.pydir.dto.AccountResponse
import de.pydir.dto.ChangeAccountInfosRequest
import de.pydir.dto.ChangePasswordRequest
import de.pydir.dto.DeleteAccountRequest
import de.pydir.entity.Account
import de.pydir.repository.AccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun getAccountDetails(user: User): AccountResponse {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        return AccountResponse.fromAccount(account)
    }

    fun changeAccountDetails(user: User, request: ChangeAccountInfosRequest): AccountResponse {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        request.username?.let { account.username = it }
        request.selectedCharacter?.let { account.selectedCharacter = it }
        request.selectedEffect?.let { account.selectedEffect = it }
        request.preferredGiftcardType?.let { account.preferredGiftcardType = it }
        request.language?.let { account.language = it }

        accountRepository.save(account)
        return AccountResponse.fromAccount(account)
    }

    fun isVerified(account: Account): Boolean {
        return account.isEmailVerified && account.isPhoneVerified
    }

    fun changePassword(user: User, request: ChangePasswordRequest) {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        if (!passwordEncoder.matches(request.oldPassword, account.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        account.password = passwordEncoder.encode(request.newPassword)

        accountRepository.save(account)
    }

    fun setIsInGame(user: User, inGame: Boolean) {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        account.isInGame = inGame
        accountRepository.save(account)
    }

    fun isInGame(user: User): Boolean? {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        return account.isInGame
    }

    fun deleteAccount(user: User, request: DeleteAccountRequest) {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        if (!passwordEncoder.matches(request.password, account.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        accountRepository.delete(account)
    }

    fun acceptTerms(user: User) {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { IllegalArgumentException("Account not found") }

        account.hasAcceptedTerms = true
        accountRepository.save(account)
    }


}
