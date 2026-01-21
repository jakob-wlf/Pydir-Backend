package de.pydir.controller

import de.pydir.service.VerificationService
import org.springframework.http.ResponseEntity
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

    // THe verification endpoints are not protected by authentication filters
    @PostMapping("/email/{accountId}")
    fun verifyEmail(@RequestParam verificationCode: String, @PathVariable accountId: Long): ResponseEntity<String> {
        val verified = verificationService.verifyMailWithCode(accountId, verificationCode)
        return if(verified) ResponseEntity.ok("Successfully verified email")
        else ResponseEntity.status(400).body("Invalid verification code")
    }

    @PostMapping("/phone/{accountId}")
    fun verifyPhone(@RequestParam verificationCode: String, @PathVariable accountId: Long): ResponseEntity<String> {
        val verified = verificationService.verifyPhoneWithCode(accountId, verificationCode)
        return if(verified) ResponseEntity.ok("Successfully verified phone number")
        else ResponseEntity.status(400).body("Invalid verification code")
    }

}