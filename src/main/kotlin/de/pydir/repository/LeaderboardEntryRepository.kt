package de.pydir.repository

import de.pydir.entity.LeaderboardEntry
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeaderboardEntryRepository : JpaRepository<LeaderboardEntry, Long> {
    fun findTop100ByOrderByBestTimeAsc(): List<LeaderboardEntry>
    fun findAllByOrderByBestTimeAsc(pageable: Pageable): List<LeaderboardEntry>
}