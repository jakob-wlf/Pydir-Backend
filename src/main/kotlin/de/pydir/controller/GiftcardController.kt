package de.pydir.controller

import de.pydir.dto.CreateGiftcardRequest
import de.pydir.dto.GetGiftCardsByTypeAndValueRequest
import de.pydir.dto.GiftcardResponse
import de.pydir.service.GiftcardService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/giftcards")
class GiftcardController(
    private val giftcardService: GiftcardService
) {

    @PostMapping("/create")
    fun createNewGiftcard(@RequestBody request: CreateGiftcardRequest): ResponseEntity<GiftcardResponse> {
        return ResponseEntity.ok(giftcardService.createNewGiftcard(request))
    }

    @GetMapping("/all")
    fun getAllGiftcards(pageable: Pageable): ResponseEntity<Page<GiftcardResponse>> {
        return ResponseEntity.ok(giftcardService.getPaginatedGiftcards(pageable))
    }

    @DeleteMapping("/delete/{giftcardId}")
    fun deleteGiftcard(@PathVariable giftcardId: Long): ResponseEntity<String> {
        giftcardService.deleteGiftcard(giftcardId)
        return ResponseEntity.ok("Successfully deleted Giftcard")
    }

    @GetMapping
    fun getGiftcardsByTypeAndValue(@RequestBody request: GetGiftCardsByTypeAndValueRequest, pageable: Pageable): ResponseEntity<Page<GiftcardResponse>> {
        return ResponseEntity.ok(giftcardService.getGiftcardsByValueAndId(request, pageable))
    }

}