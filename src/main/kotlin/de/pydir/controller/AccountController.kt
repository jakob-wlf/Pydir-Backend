package de.pydir.controller

import de.pydir.dto.AccountResponse
import de.pydir.dto.ChangeAccountInfosRequest
import de.pydir.dto.ChangePasswordRequest
import de.pydir.dto.DeleteAccountRequest
import de.pydir.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @PutMapping
    fun changeAccountInfos(@AuthenticationPrincipal user: User, @RequestBody request: ChangeAccountInfosRequest): ResponseEntity<AccountResponse> {
        return ResponseEntity.ok(accountService.changeAccountDetails(user, request))
    }

    @PutMapping("/password")
    fun changePassword(@AuthenticationPrincipal user: User, @RequestBody request: ChangePasswordRequest): ResponseEntity<String> {
        accountService.changePassword(user, request)
        return ResponseEntity.ok("Successfully changed the password")
    }

    @PutMapping("/set-ingame")
    fun setIsInGame(@AuthenticationPrincipal user: User, @RequestParam isInGame: Boolean): ResponseEntity<String> {
        accountService.setIsInGame(user, isInGame)
        return ResponseEntity.ok("Successfully set in-game status")
    }

    @GetMapping("/ingame")
    fun isInGame(@AuthenticationPrincipal user: User): ResponseEntity<Boolean> {
        return ResponseEntity.ok(accountService.isInGame(user))
    }

    @DeleteMapping("/delete-account")
    fun deleteAccount(@AuthenticationPrincipal user: User, @RequestBody request: DeleteAccountRequest): ResponseEntity<String> {
        accountService.deleteAccount(user, request)
        return ResponseEntity.ok("Successfully deleted account")
    }

    @PostMapping("/accept-terms")
    fun acceptTerms(@AuthenticationPrincipal user: User): ResponseEntity<String> {
        accountService.acceptTerms(user)
        return ResponseEntity.ok("Successfully accepted terms and conditions")
    }
}