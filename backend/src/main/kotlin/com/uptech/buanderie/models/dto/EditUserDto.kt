package com.uptech.buanderie.models.dto

data class EditUserDto (

    val id: Long = 0L,
    val firstName: String = "",
    val lastName: String = "",
    val pseudo: String = "",
    val roleIdList: List<Long> = listOf()
)