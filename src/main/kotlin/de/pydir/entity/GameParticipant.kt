package de.pydir.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "game_participants")
data class GameParticipant(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val gameSessionId: Long,  // Changed from UUID to Long

    @Column(nullable = false)
    val accountId: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ParticipantStatus = ParticipantStatus.JOINED,

    @Column(nullable = false)
    val joinedAt: Instant = Instant.now()
) {
    enum class ParticipantStatus {
        JOINED,
        COMPLETED,
        DISCONNECTED
    }
}