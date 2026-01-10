package de.pydir.entity

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "game_sessions")
data class GameSession(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val gameServerId: String,

    @Column(nullable = false, unique = true)
    val sessionToken: UUID,

    @Column(nullable = false)
    val startTime: Instant = Instant.now(),

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: SessionStatus = SessionStatus.ACTIVE
) {
    enum class SessionStatus {
        ACTIVE,
        COMPLETED,
        INVALIDATED
    }
}