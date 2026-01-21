package de.pydir.config

import de.pydir.security.GameServerApiKeyFilter
import de.pydir.security.JwtAuthenticationFilter
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableCaching
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtFilter: JwtAuthenticationFilter,
        gameServerApiKeyFilter: GameServerApiKeyFilter
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                auth.requestMatchers("/api/debug/**").permitAll() // IMPORTANT: REMOVE IN PRODUCTION
                auth.requestMatchers("/api/leaderboard/**").permitAll()
                auth.requestMatchers("/api/admin/test-connection").permitAll()
                auth.requestMatchers("/api/admin/client-version").permitAll()
                auth.requestMatchers("/api/verification/email/**").permitAll()
                auth.requestMatchers("/api/verification/phone/**").permitAll()

                // Game server endpoints - protected by API key filter
                auth.requestMatchers("/api/game-sessions/**").permitAll()

                auth.requestMatchers("/api/admin/**").hasRole("ADMIN")
                auth.requestMatchers("/api/giftcard/**").hasRole("ADMIN")
                auth.anyRequest().authenticated()
            }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(gameServerApiKeyFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager("leaderboard")
    }
}