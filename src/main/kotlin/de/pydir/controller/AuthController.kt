package de.pydir.controller

import de.pydir.dto.AuthResponse
import de.pydir.dto.LoginRequest
import de.pydir.dto.RegisterRequest
import de.pydir.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(AuthResponse(authService.register(request)))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(AuthResponse(authService.login(request)))
    }


}
