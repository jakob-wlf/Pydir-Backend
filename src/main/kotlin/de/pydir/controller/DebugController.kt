package de.pydir.controller

import de.pydir.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/debug")
class DebugController(private val adminService: AdminService) {

    @PostMapping("/make-admin/{email}")
    fun makeAdmin(@PathVariable email: String): ResponseEntity<String> {
        adminService.grantAdminRole(email)
        return ResponseEntity.ok("Admin role granted to $email")
    }
}