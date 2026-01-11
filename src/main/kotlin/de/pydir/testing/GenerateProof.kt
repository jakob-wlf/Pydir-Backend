package de.pydir.testing

import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun main() {
    // Your configuration (must match application.properties)
    val hmacSecret = "bd611021-4734-42d8-bdd6-fe71e8cb274f577d2e00-2420-4618-a607-af0e12100ff8"

    // Your test data
    val sessionToken = UUID.fromString("8c06d28f-1e55-48a1-9f4f-3d5e35bafe37") // From session creation response
    val playerTimes = listOf(
        PlayerTime(2, 47890)
    )

    // Generate proof
    val proof = generateProof(sessionToken, playerTimes, hmacSecret)

    println("Proof: $proof")
    println()
    println("Full request body:")
    println("""
    {
      "sessionToken": "$sessionToken",
      "playerTimes": [
        {"accountId": ${playerTimes[0].accountId}, "timeMs": ${playerTimes[0].timeMs}}
      ],
      "proof": "$proof"
    }
    """.trimIndent())
}

data class PlayerTime(val accountId: Long, val timeMs: Long)

fun generateProof(sessionToken: UUID, playerTimes: List<PlayerTime>, hmacSecret: String): String {
    // Build data string: "sessionToken:accountId1:time1,accountId2:time2,..."
    val data = buildString {
        append(sessionToken.toString())
        append(":")
        playerTimes.forEachIndexed { index, pt ->
            if (index > 0) append(",")
            append("${pt.accountId}:${pt.timeMs}")
        }
    }

    println("Data to hash: $data")

    return generateHMAC(data, hmacSecret)
}

fun generateHMAC(data: String, key: String): String {
    val mac = Mac.getInstance("HmacSHA256")
    val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
    mac.init(secretKey)
    val bytes = mac.doFinal(data.toByteArray())

    return bytes.joinToString("") { "%02x".format(it) }
}