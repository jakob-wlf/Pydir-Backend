package de.pydir.dto

import de.pydir.entity.Account
import java.time.Instant

data class DetailedAccountResponse(
    val id: Long,
    val email: String,
    val username: String,
    val phoneNumber: String,

    val isPhoneVerified: Boolean,
    val isEmailVerified: Boolean,

    val isLoggedInWithGoogle: Boolean,

    val roles: Set<String>,

    val isBot: Boolean,
    val hasAcceptedTerms: Boolean,
    val isSubscribed: Boolean,

    val selectedEffect: Int,
    val selectedCharacter: Int,
    val preferredGiftcardType: String,

    val rockets: Int,
    val coins: Int,
    val credits: Int,

    val createdAt: Instant,
    val lastLoginAt: Instant
) {
    companion object {
        fun fromAccount(account: Account): DetailedAccountResponse {
            return DetailedAccountResponse(
                id = account.id,
                email = account.email,
                username = account.username,
                phoneNumber = account.phoneNumber,

                isPhoneVerified = account.isPhoneVerified,
                isEmailVerified = account.isEmailVerified,

                isLoggedInWithGoogle = account.isLoggedInWithGoogle,

                roles = account.roles.map { it.name }.toSet(),

                isBot = account.isBot,
                hasAcceptedTerms = account.hasAcceptedTerms,
                isSubscribed = account.isSubscribed,

                selectedEffect = account.selectedEffect,
                selectedCharacter = account.selectedCharacter,
                preferredGiftcardType = account.preferredGiftcardType,

                rockets = account.rockets,
                coins = account.coins,
                credits = account.credits,

                createdAt = account.createdAt,
                lastLoginAt = account.lastLoggedIn
            )
        }
    }
}