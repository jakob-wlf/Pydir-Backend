package de.pydir.controller

import de.pydir.dto.AccountResponse
import de.pydir.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/account")
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping("/details")
    fun getAccountDetails(@AuthenticationPrincipal user: User): ResponseEntity<AccountResponse> {
        return ResponseEntity.ok(accountService.getAccountDetails(user))
    }

}