package de.pydir.controller

import de.pydir.service.VerificationService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/verification")
class VerificationController(
    private val verificationService: VerificationService
) {

    // The verification endpoints are not protected by authentication filters
    @GetMapping("/email/{accountId}")
    fun verifyEmail(@RequestParam verificationCode: String, @PathVariable accountId: Long): ResponseEntity<String> {
        val verified = verificationService.verifyMailWithCode(accountId, verificationCode)
        return if(verified) ResponseEntity.ok("Successfully verified email")
        else ResponseEntity.status(400).body("Invalid verification code")
    }

    @GetMapping("/phone/{accountId}")
    fun verifyPhone(@RequestParam verificationCode: String, @PathVariable accountId: Long): ResponseEntity<String> {
        val verified = verificationService.verifyPhoneWithCode(accountId, verificationCode)
        return if(verified) ResponseEntity.ok("Successfully verified phone number")
        else ResponseEntity.status(400).body("Invalid verification code")
    }

    // Protected endpoints
    @PostMapping("/resend-email")
    fun resendEmailVerification(@AuthenticationPrincipal user: User): ResponseEntity<String> {
        verificationService.resendEmailVerification(user)
        return ResponseEntity.ok("Resent email verification")
    }

    @PostMapping("/resend-phone")
    fun resendPhoneVerification(@AuthenticationPrincipal user: User): ResponseEntity<String> {
        verificationService.resendPhoneVerification(user)
        return ResponseEntity.ok("Resent phone verification")
    }

}