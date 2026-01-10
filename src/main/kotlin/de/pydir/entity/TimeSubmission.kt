package de.pydir.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "time_submissions")
data class TimeSubmission(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val gameSessionId: Long,  // Changed from UUID to Long

    @Column(nullable = false)
    val accountId: Long,

    @Column(nullable = false)
    val timeMs: Long,

    @Column(nullable = false)
    val validationProof: String,

    @Column(nullable = false)
    val submittedAt: Instant = Instant.now(),

    @Column(nullable = false)
    val verified: Boolean = false
)