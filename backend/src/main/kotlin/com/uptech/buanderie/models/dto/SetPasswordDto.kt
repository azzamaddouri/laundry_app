package com.uptech.buanderie.models.dto


data class SetPasswordDto (
    val token: String = "",
    val password: String = ""
)