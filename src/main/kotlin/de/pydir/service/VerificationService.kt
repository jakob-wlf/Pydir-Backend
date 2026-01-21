package de.pydir.service

import de.pydir.entity.Account
import de.pydir.entity.Verification
import de.pydir.exception.AccountNotFoundException
import de.pydir.exception.VerificationException
import de.pydir.repository.AccountRepository
import de.pydir.repository.VerificationRepository
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VerificationService(
    private val verificationRepository: VerificationRepository,
    private val accountRepository: AccountRepository,
    private val securityService: SecurityService,
    private val messagingService: MessagingService
) {
    companion object {
        const val VERIFICATION_EXPIRY_MS = 15 * 60 * 1000L // 15 minutes
        const val RESEND_COOLDOWN_MS = 60 * 1000L // 1 minute between resends
        const val MAX_ATTEMPTS_PER_HOUR = 5
    }

    @Transactional
    fun createVerification(account: Account) {
        createVerificationForType(account, Verification.VerficationType.EMAIL)
        createVerificationForType(account, Verification.VerficationType.PHONE)
    }

    @Transactional
    fun verifyPhoneWithCode(accountId: Long, code: String): Boolean {
        return verifyWithCode(accountId, code, Verification.VerficationType.PHONE)
    }

    @Transactional
    fun verifyMailWithCode(accountId: Long, code: String): Boolean {
        return verifyWithCode(accountId, code, Verification.VerficationType.EMAIL)
    }

    @Transactional
    fun resendPhoneVerification(user: User) {
        resendVerification(user, Verification.VerficationType.PHONE)
    }

    @Transactional
    fun resendEmailVerification(user: User) {
        resendVerification(user, Verification.VerficationType.EMAIL)
    }

    private fun createVerificationForType(account: Account, type: Verification.VerficationType) {
        // Delete any existing verifications of this type
        val existingVerifications = verificationRepository.findByAccountIdAndType(account.id, type)
        if (existingVerifications.isNotEmpty()) {
            verificationRepository.deleteAll(existingVerifications)
        }

        val verification = Verification(
            accountId = account.id,
            token = securityService.generateVerificationCode(),
            type = type,
            createdAtMs = System.currentTimeMillis(),
            expiresAtMs = System.currentTimeMillis() + VERIFICATION_EXPIRY_MS
        )

        verificationRepository.save(verification)

        // Send to user
        when (type) {
            Verification.VerficationType.EMAIL -> {}
            Verification.VerficationType.PHONE -> messagingService.sendVerificationSms(account.phoneNumber, verification.token)
        }
    }

    private fun verifyWithCode(accountId: Long, code: String, type: Verification.VerficationType): Boolean {
        val account = accountRepository.findById(accountId)
            .orElseThrow { AccountNotFoundException() }

        // Clean up expired verifications
        cleanupExpiredVerifications(account.id, type)

        val verification = verificationRepository.findByAccountIdAndType(account.id, type)
            .find { it.token == code && !it.isExpired() } ?: return false

        verificationRepository.delete(verification)
        when (type) {
            Verification.VerficationType.EMAIL -> account.isEmailVerified = true
            Verification.VerficationType.PHONE -> account.isPhoneVerified = true
        }
        return true
    }

    fun verifyAccount(account: Account) {
        account.isEmailVerified = true
        account.isPhoneVerified = true
        accountRepository.save(account)

        verificationRepository.deleteAll(
            verificationRepository.findByAccountIdAndType(account.id, Verification.VerficationType.EMAIL) +
            verificationRepository.findByAccountIdAndType(account.id, Verification.VerficationType.PHONE)
        )
    }

    fun verifyAccount(account: Account, type: Verification.VerficationType) {
        when (type) {
            Verification.VerficationType.EMAIL -> account.isEmailVerified = true
            Verification.VerficationType.PHONE -> account.isPhoneVerified = true
        }
        accountRepository.save(account)

        verificationRepository.deleteAll(
            verificationRepository.findByAccountIdAndType(account.id, type)
        )
    }

    private fun resendVerification(user: User, type: Verification.VerficationType) {
        val account = accountRepository.findByEmail(user.username)
            .orElseThrow { AccountNotFoundException() }

        // Check rate limiting
        checkRateLimits(account.id, type)

        // Clean up expired verifications first
        cleanupExpiredVerifications(account.id, type)

        // Check if there's a recent verification within cooldown period
        val recentVerifications = verificationRepository.findByAccountIdAndType(account.id, type)
        val mostRecent = recentVerifications.maxByOrNull { it.createdAtMs }

        if (mostRecent != null && System.currentTimeMillis() - mostRecent.createdAtMs < RESEND_COOLDOWN_MS) {
            throw VerificationException("Please wait before requesting another code")
        }

        // Delete old verifications and create new one
        if (recentVerifications.isNotEmpty()) {
            verificationRepository.deleteAll(recentVerifications)
        }

        createVerificationForType(account, type)
    }

    private fun checkRateLimits(accountId: Long, type: Verification.VerficationType) {
        val oneHourAgo = System.currentTimeMillis() - (60 * 60 * 1000L)
        val recentAttempts = verificationRepository.findByAccountIdAndTypeAndCreatedAtMsGreaterThan(
            accountId, type, oneHourAgo
        )

        if (recentAttempts.size >= MAX_ATTEMPTS_PER_HOUR) {
            throw VerificationException("Too many verification attempts. Please try again later")
        }
    }

    private fun cleanupExpiredVerifications(accountId: Long, type: Verification.VerficationType) {
        val verifications = verificationRepository.findByAccountIdAndType(accountId, type)
        val expired = verifications.filter { it.isExpired() }
        if (expired.isNotEmpty()) {
            verificationRepository.deleteAll(expired)
        }
    }
}