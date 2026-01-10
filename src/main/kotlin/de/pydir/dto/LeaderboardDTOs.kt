package de.pydir.dto

import java.time.Instant
import java.util.*

data class PlayerTime(
    val accountId: Long,
    val timeMs: Long
)

data class SubmitTimesRequest(
    val sessionToken: UUID,
    val playerTimes: List<PlayerTime>,
    val proof: String
)

data class LeaderboardEntryDTO(
    val rank: Int,
    val accountId: Long,
    val username: String,
    val bestTime: Long,
    val totalRuns: Int,
    val achievedAt: Instant
)

data class PlayerStatsDTO(
    val accountId: Long,
    val username: String,
    val bestTime: Long?,
    val totalRuns: Int,
    val averageTime: Long?,
    val recentSubmissions: List<RecentSubmissionDTO>
)

data class RecentSubmissionDTO(
    val timeMs: Long,
    val submittedAt: Instant,
    val sessionId: Long
)