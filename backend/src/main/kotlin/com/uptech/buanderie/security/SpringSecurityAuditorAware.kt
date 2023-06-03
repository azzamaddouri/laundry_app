package com.uptech.buanderie.security


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import com.uptech.buanderie.repositories.UserRepository
import com.uptech.buanderie.models.entity.User
import java.util.*

open class SpringSecurityAuditorAware : AuditorAware<User> {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun getCurrentAuditor(): Optional<User> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated) {
            return Optional.empty()
        }
        val appUser = userRepository.findUserByEmail((authentication.principal as UserDetailsImpl).username)
        return if (appUser.isPresent) {
            appUser
        } else Optional.empty()
    }


}