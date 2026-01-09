package de.pydir.service

import de.pydir.dto.LogResponse
import de.pydir.entity.LogEntry
import de.pydir.repository.LogRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class Logger(
    private val logRepository: LogRepository
) {
    fun log(message: String) {
        val logEntry = LogEntry(
            message = message,
            level = LogLevel.INFO.name,
            stackTrace = ""
        )
        logRepository.save(logEntry)
    }

    fun logWarning(message: String) {
        val logEntry = LogEntry(
            message = message,
            level = LogLevel.WARN.name,
            stackTrace = ""
        )
        logRepository.save(logEntry)
    }

    fun logError(message: String, exception: Exception) {
        val logEntry = LogEntry(
            message = message,
            level = LogLevel.ERROR.name,
            stackTrace = exception.stackTraceToString()
        )
        logRepository.save(logEntry)
    }

    fun getPaginatedLogs(pageable: Pageable): Page<LogResponse> {
        return logRepository.findAll(pageable).map { LogResponse.fromLogEntry(it) }
    }

    enum class LogLevel(private val levelName: String) {
        INFO("Info"),
        WARN("Warn"),
        ERROR("Error");

    }
}