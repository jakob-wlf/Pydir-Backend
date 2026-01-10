package de.pydir.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "leaderboard_entries")
data class LeaderboardEntry(
    @Id
    val accountId: Long,

    @Column(nullable = false)
    var bestTime: Long,

    @Column(nullable = false)
    var totalRuns: Int = 0,

    @Column(nullable = false)
    var achievedAt: Instant = Instant.now(),

    @Column(nullable = false)
    var sourceSessionId: Long  // Reference to GameSession
)