package com.uptech.buanderie.repositories

import com.uptech.buanderie.models.entity.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface ResetPasswordTokenRepository : JpaRepository<PasswordResetToken, Long?> {
    fun findByToken(token: String): Optional<PasswordResetToken>
    fun deleteByCreatedAtBefore(date: Date)
}