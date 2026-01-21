package de.pydir.service

import de.pydir.dto.*
import de.pydir.entity.GameParticipant
import de.pydir.entity.GameSession
import de.pydir.entity.TimeSubmission
import de.pydir.exception.*
import de.pydir.repository.AccountRepository
import de.pydir.repository.GameParticipantRepository
import de.pydir.repository.GameSessionRepository
import de.pydir.repository.TimeSubmissionRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class GameSessionService(
    private val gameSessionRepo: GameSessionRepository,
    private val participantRepo: GameParticipantRepository,
    private val timeSubmissionRepo: TimeSubmissionRepository,
    private val accountRepo: AccountRepository,
    private val leaderboardService: LeaderboardService,
    private val accountService: AccountService,
    @Value("\${game.server.hmac.secret}")
    private val hmacSecret: String
) {

    fun createSession(): GameSessionResponse {
        val sessionToken = UUID.randomUUID()
        
        val session = GameSession(
            gameServerId = "default-server",  // Since all servers use same API key
            sessionToken = sessionToken,
            status = GameSession.SessionStatus.ACTIVE
        )
        
        val saved = gameSessionRepo.save(session)
        
        return GameSessionResponse(
            sessionId = saved.id,
            sessionToken = sessionToken
        )
    }

    @Transactional
    fun joinSession(sessionId: Long, request: JoinRequest) {
        // Verify session exists and is active
        val session = gameSessionRepo.findById(sessionId)
            .orElseThrow { SessionNotFoundException() }
        
        if (session.status != GameSession.SessionStatus.ACTIVE) {
            throw SessionNotActiveException()
        }
        
        // Verify account exists
        val account = accountRepo.findById(request.accountId)
            .orElseThrow { AccountNotFoundException() }

        // Check if account is verified
        if (!accountService.isVerified(account)) {
            throw AccountNotVerifiedException()
        }
        
        // Check if already joined
        if (participantRepo.existsByGameSessionIdAndAccountId(sessionId, request.accountId)) {
            throw AlreadyJoinedException()
        }
        
        // Create participant
        val participant = GameParticipant(
            gameSessionId = sessionId,
            accountId = request.accountId,
            status = GameParticipant.ParticipantStatus.JOINED
        )
        
        participantRepo.save(participant)
    }

    @Transactional
    fun submitTimes(sessionId: Long, request: SubmitTimesRequest) {
        // 1. Verify session exists and is active
        val session = gameSessionRepo.findById(sessionId)
            .orElseThrow { SessionNotFoundException() }
        
        if (session.status != GameSession.SessionStatus.ACTIVE) {
            throw SessionNotActiveException()
        }
        
        // 2. Verify session token matches
        if (session.sessionToken != request.sessionToken) {
            throw InvalidSessionTokenException()
        }
        
        // 3. Verify all players were in this session
        request.playerTimes.forEach { playerTime ->
            val wasInSession = participantRepo.existsByGameSessionIdAndAccountId(
                sessionId, 
                playerTime.accountId
            )
            
            if (!wasInSession) {
                throw PlayerNotInSessionException(playerTime.accountId)
            }
        }
        
        // 4. Verify cryptographic proof
        val expectedProof = generateProof(request.sessionToken, request.playerTimes)
        
        if (expectedProof != request.proof) {
            throw InvalidProofException()
        }
        
        // 5. All validations passed - save submissions
        request.playerTimes.forEach { playerTime ->
            val submission = TimeSubmission(
                gameSessionId = sessionId,
                accountId = playerTime.accountId,
                timeMs = playerTime.timeMs,
                validationProof = request.proof,
                verified = true
            )
            
            timeSubmissionRepo.save(submission)
            
            // Update leaderboard
            leaderboardService.updateIfBest(
                playerTime.accountId, 
                playerTime.timeMs, 
                sessionId
            )
        }
        
        // 6. Mark session as completed
        session.status = GameSession.SessionStatus.COMPLETED
        gameSessionRepo.save(session)
    }

    private fun generateProof(sessionToken: UUID, playerTimes: List<PlayerTime>): String {
        // Build data string: "sessionToken:accountId1:time1,accountId2:time2,..."
        val data = buildString {
            append(sessionToken.toString())
            append(":")
            playerTimes.forEachIndexed { index, pt ->
                if (index > 0) append(",")
                append("${pt.accountId}:${pt.timeMs}")
            }
        }
        
        return generateHMAC(data, hmacSecret)
    }

    private fun generateHMAC(data: String, key: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
        mac.init(secretKey)
        val bytes = mac.doFinal(data.toByteArray())
        
        return bytes.joinToString("") { "%02x".format(it) }
    }
}