package de.pydir.dto

import de.pydir.entity.LogEntry
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.Instant

data class LogResponse(
    val message: String,
    val level: String,
    val stackTrace: String,
    val timestamp: Instant = Instant.now(),
) {
    companion object {
        fun fromLogEntry(logEntry: LogEntry): LogResponse {
            return LogResponse(
                message = logEntry.message,
                level = logEntry.level,
                stackTrace = logEntry.stackTrace,
                timestamp = logEntry.timestamp
            )
        }
    }
}