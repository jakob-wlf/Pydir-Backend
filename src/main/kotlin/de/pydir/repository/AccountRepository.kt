package de.pydir.repository

import de.pydir.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Optional<Account>
    fun findByUsername(username: String): Optional<Account>
}
