package de.pydir.repository

import de.pydir.entity.GameSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface GameSessionRepository : JpaRepository<GameSession, Long> {
    fun findBySessionToken(sessionToken: UUID): GameSession?
    fun findByStatusAndStartTimeBefore(
        status: GameSession.SessionStatus,
        time: Instant
    ): List<GameSession>
}