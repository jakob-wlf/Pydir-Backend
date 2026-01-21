package de.pydir.service

import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class SecurityService {

    fun generateVerificationCode(): String {
        val codeLength = 6
        val random = SecureRandom()
        return (1..codeLength)
            .map { random.nextInt(10) }
            .joinToString("")
    }

}