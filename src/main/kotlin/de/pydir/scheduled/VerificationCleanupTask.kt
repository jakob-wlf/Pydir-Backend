package de.pydir.scheduled

import de.pydir.repository.VerificationRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class VerificationCleanupTask(
    private val verificationRepository: VerificationRepository
) {
    
    @Scheduled(cron = "0 0 * * * *") // Every hour at minute 0
    @Transactional
    fun cleanupExpiredVerifications() {
        val cutoffTime = System.currentTimeMillis()
        val deleted = verificationRepository.deleteByExpiresAtMsLessThan(cutoffTime)
        if (deleted > 0) {
            println("Cleaned up $deleted expired verifications")
        }
    }
}