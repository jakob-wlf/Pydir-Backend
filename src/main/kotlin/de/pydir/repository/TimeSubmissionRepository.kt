package de.pydir.repository

import de.pydir.entity.TimeSubmission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TimeSubmissionRepository : JpaRepository<TimeSubmission, Long> {
    fun findByAccountIdOrderBySubmittedAtDesc(accountId: Long): List<TimeSubmission>
    fun findByGameSessionId(gameSessionId: Long): List<TimeSubmission>
}