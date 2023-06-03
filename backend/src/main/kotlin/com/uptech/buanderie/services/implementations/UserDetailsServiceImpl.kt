package com.uptech.buanderie.services.implementations

import com.uptech.buanderie.extentions.isValidEmail
import kotlin.Throws
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import com.uptech.buanderie.models.entity.User
import com.uptech.buanderie.repositories.UserRepository
import com.uptech.buanderie.security.SecurityUtils
import java.util.*
import java.util.function.Supplier
import javax.transaction.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(userName: String): UserDetails {
        lateinit var user : Optional<User>
        if (userName.isValidEmail()){
            user = userRepository.findUserByEmail(userName)

        } else {
            user = userRepository.findUserByPseudo(userName)
        }

        if (!user.isPresent) {
            throw UsernameNotFoundException(
                "User Not Found with userName: $userName"
            )
        }

        if (!user.get().enabled || user.get().archived) {
            throw UsernameNotFoundException(
                "User unauthorized with userName: $userName"
            )
        }

        return SecurityUtils.build(user.get())
    }
}