package com.uptech.buanderie.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import com.uptech.buanderie.models.entity.User
import com.uptech.buanderie.security.SpringSecurityAuditorAware

@Configuration
@EnableJpaAuditing
internal class JpaConfig {
    @Bean
    fun auditorProvider(): AuditorAware<User> {
        return SpringSecurityAuditorAware()
    }

}