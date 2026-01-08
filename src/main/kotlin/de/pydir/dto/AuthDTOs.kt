package de.pydir.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val phoneNumber: String,
    val username: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val message: String
)
