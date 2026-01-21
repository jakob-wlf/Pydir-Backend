package de.pydir.controller

import de.pydir.dto.DetailedAccountResponse
import de.pydir.dto.LogResponse
import de.pydir.entity.LogEntry
import de.pydir.entity.Verification
import de.pydir.service.AdminService
import de.pydir.service.Logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService,
    private val logger: Logger
) {

    @GetMapping("/details/{accountId}")
    fun getAccountDetails(@PathVariable accountId: Long): ResponseEntity<DetailedAccountResponse> {
        return ResponseEntity.ok(adminService.getAccountDetails(accountId))
    }

    @GetMapping("/accountId/{username}")
    fun getAccountIdByUsername(@PathVariable username: String): ResponseEntity<Long> {
        return ResponseEntity.ok(adminService.getAccountIdByUsername(username))
    }

    @GetMapping("/accounts")
    fun getAllAccounts(
        pageable: Pageable,
        @RequestParam(required = false) includeBots: Boolean = false
    ): ResponseEntity<Page<DetailedAccountResponse>> {
        return ResponseEntity.ok(adminService.getPaginatedAccounts(pageable, includeBots))
    }

    @DeleteMapping("/delete/{accountId}")
    fun deleteAccount(@PathVariable accountId: Long): ResponseEntity<String> {
        adminService.deleteAccount(accountId)
        return ResponseEntity.ok("Successfully deleted account")
    }

    @PostMapping("/verify-phone/{accountId}")
    fun verifyPhoneNumber(@PathVariable accountId: Long): ResponseEntity<String> {
        adminService.verifyAccount(accountId, Verification.VerficationType.PHONE)
        return ResponseEntity.ok("Successfully verified phone number")
    }

    @PostMapping("/verify-mail/{accountId}")
    fun verifyEmail(@PathVariable accountId: Long): ResponseEntity<String> {
        adminService.verifyAccount(accountId, Verification.VerficationType.EMAIL)
        return ResponseEntity.ok("Successfully verified phone number")
    }

    @PostMapping("/verify/{accountId}")
    fun verifyAccount(@PathVariable accountId: Long): ResponseEntity<String> {
        adminService.verifyAccount(accountId)
        return ResponseEntity.ok("Successfully verified phone number")
    }

    @DeleteMapping("/delete-all")
    fun deleteAllAccounts(): ResponseEntity<String> {
        adminService.deleteAllAccounts()
        return ResponseEntity.ok("Successfully deleted all accounts")
    }

    @GetMapping("/logs")
    fun getLogs(
        @PageableDefault(size = 50, page = 0, sort = ["timestamp"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<LogResponse>> {
        return ResponseEntity.ok(logger.getPaginatedLogs(pageable))
    }

    // Is excluded from authentication in SecurityConfig
    @GetMapping("/test-connection")
    fun testConnection(): ResponseEntity<String> {
        return ResponseEntity.ok("")
    }


    // Is excluded from authentication in SecurityConfig
    @GetMapping("client-version")
    fun getClientVersion(): ResponseEntity<String> {
        return ResponseEntity.ok(adminService.getClientVersion())
    }
}