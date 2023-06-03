package com.uptech.buanderie.models.dto


data class RegisterDto (
    val firstName: String = "",
    val lastName: String = "",
    val pseudo: String = "",
    val email: String = "",
    val pwd: String = ""
)