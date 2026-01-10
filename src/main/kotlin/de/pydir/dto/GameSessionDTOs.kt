package de.pydir.dto

import java.util.*

data class CreateSessionRequest(
    val gameMode: String? = null,
    val maxPlayers: Int = 4
)

data class JoinRequest(
    val accountId: Long
)

data class GameSessionResponse(
    val sessionId: Long,
    val sessionToken: UUID
)