package de.pydir.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "log_entries")
data class LogEntry(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val message: String,

    @Column(nullable = false)
    val level: String,

    @Column(nullable = false, length = 4000)
    val stackTrace: String,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now(),
)