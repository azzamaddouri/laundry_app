package com.uptech.buanderie.repositories

import org.springframework.data.jpa.repository.JpaRepository
import com.uptech.buanderie.models.entity.UserInfo

interface UserInfoRepository : JpaRepository<UserInfo, Long> {
}