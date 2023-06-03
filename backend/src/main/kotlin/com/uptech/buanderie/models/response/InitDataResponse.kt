package com.uptech.buanderie.models.response

import com.uptech.buanderie.models.entity.*

data class InitDataResponse(
    var userInfo: UserInfo = UserInfo(),
    var roleList: List<Role> = listOf(),

    ) {

}