package com.uptech.buanderie.models.dto


data class ResetPasswordDto (
    val token: String = "",
    val newPassword: String = ""
)