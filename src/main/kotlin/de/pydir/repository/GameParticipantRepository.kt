package de.pydir.repository

import de.pydir.entity.GameParticipant
import de.pydir.entity.GameSession
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameParticipantRepository : JpaRepository<GameParticipant, Long> {
    fun existsByGameSessionIdAndAccountId(gameSessionId: Long, accountId: Long): Boolean
    fun findByGameSessionId(gameSessionId: Long): List<GameParticipant>
}