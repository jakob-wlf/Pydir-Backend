package de.pydir.controller

import de.pydir.dto.CreateSessionRequest
import de.pydir.dto.GameSessionResponse
import de.pydir.dto.JoinRequest
import de.pydir.dto.SubmitTimesRequest
import de.pydir.service.GameSessionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/game-sessions")
class GameSessionController(
    private val gameSessionService: GameSessionService
) {

    @PostMapping
    fun createSession(@RequestBody request: CreateSessionRequest): GameSessionResponse {
        return gameSessionService.createSession(request)
    }

    @PostMapping("/{sessionId}/join")
    fun joinSession(
        @PathVariable sessionId: Long,
        @RequestBody request: JoinRequest
    ): ResponseEntity<Void> {
        gameSessionService.joinSession(sessionId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{sessionId}/submit")
    fun submitTimes(
        @PathVariable sessionId: Long,
        @RequestBody request: SubmitTimesRequest
    ): ResponseEntity<Void> {
        gameSessionService.submitTimes(sessionId, request)
        return ResponseEntity.ok().build()
    }
}