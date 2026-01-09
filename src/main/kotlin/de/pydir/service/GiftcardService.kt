package de.pydir.service

import de.pydir.dto.CreateGiftcardRequest
import de.pydir.dto.GetGiftCardsByTypeAndValueRequest
import de.pydir.dto.GiftcardResponse
import de.pydir.entity.Giftcard
import de.pydir.repository.GiftcardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class GiftcardService(
    private val giftcardRepository: GiftcardRepository
) {

    fun createNewGiftcard(request: CreateGiftcardRequest): GiftcardResponse {
        val giftcard: Giftcard = Giftcard(
            type = request.type,
            euroValue = request.euroValue,
            code = request.code
        )

        giftcardRepository.save(giftcard)
        return GiftcardResponse.fromGiftcard(giftcard);
    }

    fun getPaginatedGiftcards(pageable: Pageable): Page<GiftcardResponse> {
        return giftcardRepository.findAll(pageable).map { GiftcardResponse.fromGiftcard(it) }
    }

    fun deleteGiftcard(giftcardId: Long) {
        val giftcard = giftcardRepository.findById(giftcardId)
            .orElseThrow { IllegalArgumentException("Giftcard not found") }

        giftcardRepository.delete(giftcard)
    }

    fun getGiftcardsByValueAndId(
        request: GetGiftCardsByTypeAndValueRequest,
        pageable: Pageable
    ): Page<GiftcardResponse>? {
        return giftcardRepository.findByTypeAndEuroValue(
            type = request.type,
            euroValue = request.euroValue,
            pageable = pageable).map { GiftcardResponse.fromGiftcard(it) }
    }
}