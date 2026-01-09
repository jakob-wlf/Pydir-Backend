package de.pydir.dto

import de.pydir.entity.Giftcard
import java.time.Instant

data class CreateGiftcardRequest(
    val type: String,
    val euroValue: Int,
    val code: String
)

data class GetGiftCardsByTypeAndValueRequest(
    val type: String,
    val euroValue: Int
)

data class GiftcardResponse(
    val id: Long,
    val type: String,
    val euroValue: Int,
    val code: String,
    val wasRedeemed: Boolean,
    val createdAt: Instant
) {
    companion object {
        fun fromGiftcard(giftcard: Giftcard): GiftcardResponse {
            return GiftcardResponse(
                id = giftcard.id,
                type = giftcard.type,
                euroValue = giftcard.euroValue,
                code = giftcard.code,
                wasRedeemed = giftcard.wasRedeemed,
                createdAt = giftcard.createdAt
            )
        }
    }
}