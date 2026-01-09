package de.pydir.repository

import de.pydir.entity.Giftcard
import de.pydir.entity.Role
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface GiftcardRepository : JpaRepository<Giftcard, Long> {
    fun findByTypeAndEuroValue(
        type: String,
        euroValue: Int,
        pageable: Pageable
    ): Page<Giftcard>

}