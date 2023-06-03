package com.uptech.buanderie.config


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import com.uptech.buanderie.services.IUserService

@Component
class ScheduledTasks {
    @Autowired
    private lateinit var userService: IUserService

    @Scheduled(cron = "0 0 0 * * *")
    fun purgeDataBase() {
        userService.deleteResetPasswordTokenExpiredTokens()
        userService.deleteSetPasswordTokenExpiredTokens()
    }

}