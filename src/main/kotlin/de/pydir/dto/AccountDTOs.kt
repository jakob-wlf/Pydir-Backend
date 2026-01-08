package de.pydir.dto

import de.pydir.entity.Account

data class AccountResponse(
    val email: String,
    val username: String,
    val isPhoneVerified: Boolean,
    val isEmailVerified: Boolean,
    val isSubscribed: Boolean,
    val selectedEffect: Int,
    val selectedCharacter: Int,
    val preferredGiftcardType: String,
    val rockets: Int,
    val coins: Int,
    val credits: Int
) {
    companion object {
        fun fromAccount(account: Account): AccountResponse {
            return AccountResponse(
                email = account.email,
                username = account.username,
                isPhoneVerified = account.isPhoneVerified,
                isEmailVerified = account.isEmailVerified,
                isSubscribed = account.isSubscribed,
                selectedEffect = account.selectedEffect,
                selectedCharacter = account.selectedCharacter,
                preferredGiftcardType = account.preferredGiftcardType,
                rockets = account.rockets,
                coins = account.coins,
                credits = account.credits
            )
        }
    }
}