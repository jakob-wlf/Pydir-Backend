package de.pydir.service

import de.pydir.dto.LeaderboardEntryDTO
import de.pydir.dto.PlayerStatsDTO
import de.pydir.dto.RecentSubmissionDTO
import de.pydir.entity.LeaderboardEntry
import de.pydir.exception.AccountNotFoundException
import de.pydir.repository.AccountRepository
import de.pydir.repository.LeaderboardEntryRepository
import de.pydir.repository.TimeSubmissionRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class LeaderboardService(
    private val leaderboardRepo: LeaderboardEntryRepository,
    private val timeSubmissionRepo: TimeSubmissionRepository,
    private val accountRepo: AccountRepository,
    private val cacheManager: CacheManager
) {

    @Transactional
    fun updateIfBest(accountId: Long, newTime: Long, sessionId: Long) {
        // Verify account exists
        accountRepo.findById(accountId)
            .orElseThrow { AccountNotFoundException() }

        val existingEntry = leaderboardRepo.findById(accountId).orElse(null)

        if (existingEntry != null) {
            // Update existing entry
            if (newTime < existingEntry.bestTime) {
                // New personal best!
                existingEntry.bestTime = newTime
                existingEntry.achievedAt = Instant.now()
                existingEntry.sourceSessionId = sessionId
            }
            existingEntry.totalRuns++
            leaderboardRepo.save(existingEntry)
            
            // Clear cache
            cacheManager.getCache("leaderboard")?.clear()
        } else {
            // Create new entry
            val newEntry = LeaderboardEntry(
                accountId = accountId,
                bestTime = newTime,
                totalRuns = 1,
                achievedAt = Instant.now(),
                sourceSessionId = sessionId
            )
            leaderboardRepo.save(newEntry)
            
            // Clear cache
            cacheManager.getCache("leaderboard")?.clear()
        }
    }

    @Cacheable("leaderboard")
    fun getLeaderboard(limit: Int): List<LeaderboardEntryDTO> {
        val entries = if (limit > 0) {
            leaderboardRepo.findAllByOrderByBestTimeAsc(PageRequest.of(0, limit))
        } else {
            leaderboardRepo.findTop100ByOrderByBestTimeAsc()
        }

        return entries.mapIndexed { index, entry ->
            val account = accountRepo.findById(entry.accountId)
                .orElseThrow { AccountNotFoundException() }

            LeaderboardEntryDTO(
                rank = index + 1,
                accountId = entry.accountId,
                username = account.username,
                bestTime = entry.bestTime,
                totalRuns = entry.totalRuns,
                achievedAt = entry.achievedAt
            )
        }
    }

    fun getPlayerStats(accountId: Long): PlayerStatsDTO {
        val account = accountRepo.findById(accountId)
            .orElseThrow { AccountNotFoundException() }

        val leaderboardEntry = leaderboardRepo.findById(accountId).orElse(null)
        val submissions = timeSubmissionRepo.findByAccountIdOrderBySubmittedAtDesc(accountId)

        val recentSubmissions = submissions.take(10).map {
            RecentSubmissionDTO(
                timeMs = it.timeMs,
                submittedAt = it.submittedAt,
                sessionId = it.gameSessionId
            )
        }

        val averageTime = if (submissions.isNotEmpty()) {
            submissions.map { it.timeMs }.average().toLong()
        } else null

        return PlayerStatsDTO(
            accountId = accountId,
            username = account.username,
            bestTime = leaderboardEntry?.bestTime,
            totalRuns = leaderboardEntry?.totalRuns ?: 0,
            averageTime = averageTime,
            recentSubmissions = recentSubmissions
        )
    }
}