package de.pydir.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(ex.message ?: "Invalid request"))
    }

    @ExceptionHandler(SessionNotFoundException::class)
    fun handleSessionNotFound(ex: SessionNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(ex.message ?: "Session not found"))
    }

    @ExceptionHandler(
        SessionNotActiveException::class,
        AlreadyJoinedException::class,
        InvalidSessionTokenException::class,
        InvalidProofException::class,
        PlayerNotInSessionException::class,
        AccountNotVerifiedException::class,
        VerificationException::class
    )
    fun handleBadRequest(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(ex.message ?: "Bad request"))
    }

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFound(ex: AccountNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse(ex.message ?: "Account not found"))
    }

    data class ErrorResponse(val error: String)
}
