package de.pydir.repository

import de.pydir.entity.LogEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository: JpaRepository<LogEntry, Long> {

}