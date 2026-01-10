package de.pydir.controller

import de.pydir.dto.LeaderboardEntryDTO
import de.pydir.dto.PlayerStatsDTO
import de.pydir.service.LeaderboardService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/leaderboard")
class LeaderboardController(
    private val leaderboardService: LeaderboardService
) {

    @GetMapping
    fun getLeaderboard(
        @RequestParam(defaultValue = "100") limit: Int
    ): List<LeaderboardEntryDTO> {
        return leaderboardService.getLeaderboard(limit)
    }

    @GetMapping("/player/{accountId}")
    fun getPlayerStats(@PathVariable accountId: Long): PlayerStatsDTO {
        return leaderboardService.getPlayerStats(accountId)
    }
}