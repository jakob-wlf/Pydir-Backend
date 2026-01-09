package de.pydir.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "giftcards")
data class Giftcard(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val type: String,

    @Column(nullable = false)
    val euroValue: Int,

    @Column(nullable = false, unique = true)
    val code: String,

    @Column(nullable = false)
    val wasRedeemed: Boolean = false,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),
)
