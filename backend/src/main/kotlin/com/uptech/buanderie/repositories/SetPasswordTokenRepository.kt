package com.uptech.buanderie.repositories

import org.springframework.data.jpa.repository.JpaRepository
import com.uptech.buanderie.models.entity.PasswordSetToken
import java.util.*


interface SetPasswordTokenRepository : JpaRepository<PasswordSetToken, Long?> {
    fun findByToken(token: String): Optional<PasswordSetToken>
    fun findByUserId(id: Long): Optional<PasswordSetToken>
    fun deleteByCreatedAtBefore(date: Date)
}