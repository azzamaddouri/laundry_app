package com.uptech.buanderie.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import com.uptech.buanderie.models.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import com.uptech.buanderie.models.entity.Role
import java.util.*


interface UserRepository : JpaRepository<User, Long> {
    fun findUserByEmail(email: String): Optional<User>
    fun findUserByPseudo(pseudo: String): Optional<User>
    fun findAllByRoleListNotContaining(role: Role, pageable: Pageable ): Page<User>
    fun findAllByRoleListContainingAndArchived(role: Role,boolean: Boolean): List<User>
}