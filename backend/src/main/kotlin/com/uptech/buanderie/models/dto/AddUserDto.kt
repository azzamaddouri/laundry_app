package com.uptech.buanderie.models.dto


data class AddUserDto (
    val firstName: String = "",
    val lastName: String = "",
    val pseudo: String = "",
    val email: String = "",
    val roleIdList: List<Long> = listOf()
)