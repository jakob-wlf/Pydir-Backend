package de.pydir.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class GameServerApiKeyFilter(
    @Value("\${game.server.api.key}")
    private val validApiKey: String
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.requestURI
        
        // Only check API key for game server endpoints
        if (path.startsWith("/api/game-sessions")) {
            val providedApiKey = request.getHeader("X-Game-Server-API-Key")
            
            if (providedApiKey == null || providedApiKey != validApiKey) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                response.writer.write("""{"error": "Invalid or missing API key"}""")
                return
            }
        }
        
        filterChain.doFilter(request, response)
    }
}