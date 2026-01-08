package de.pydir.controller

import de.pydir.dto.DetailedAccountResponse
import de.pydir.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val adminService: AdminService
) {

    @GetMapping("details/{accountId}")
    fun getAccountDetails(@PathVariable accountId: Long): ResponseEntity<DetailedAccountResponse> {
        return ResponseEntity.ok(adminService.getAccountDetails(accountId))
    }

    @GetMapping("accountId/{username}")
    fun getAccountIdByUsername(@PathVariable username: String): ResponseEntity<Long> {
        println("Received request for account ID of username: $username")
        return ResponseEntity.ok(adminService.getAccountIdByUsername(username))
    }

}