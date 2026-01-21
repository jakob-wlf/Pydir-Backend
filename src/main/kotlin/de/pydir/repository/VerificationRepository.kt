package de.pydir.repository

import de.pydir.entity.Verification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationRepository : JpaRepository<Verification, Long> {
    fun findByAccountIdAndType(accountId: Long, type: Verification.VerficationType): List<Verification>
    fun findByAccountIdAndTypeAndCreatedAtMsGreaterThan(
        accountId: Long,
        type: Verification.VerficationType,
        createdAtMs: Long
    ): List<Verification>
    fun deleteByExpiresAtMsLessThan(expiresAtMs: Long): Int
}