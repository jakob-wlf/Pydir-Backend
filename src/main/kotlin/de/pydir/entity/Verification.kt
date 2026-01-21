package de.pydir.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "verifications",
    indexes = [
        Index(name = "idx_account_type", columnList = "accountId,type"),
        Index(name = "idx_expires_at", columnList = "expiresAtMs")
    ]
)
data class Verification(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val accountId: Long,

    @Column(nullable = false)
    val token: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: VerficationType,

    @Column(nullable = false)
    val createdAtMs: Long,

    @Column(nullable = false)
    val expiresAtMs: Long

) {
    enum class VerficationType {
        EMAIL,
        PHONE
    }

    fun isExpired(): Boolean = System.currentTimeMillis() > expiresAtMs
}