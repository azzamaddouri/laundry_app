package com.uptech.buanderie.repositories

import com.uptech.buanderie.models.entity.Role
import org.springframework.data.jpa.repository.JpaRepository


interface RoleRepository : JpaRepository<Role, Long> {
    fun findRoleByLibelle(libelle: String): Role
    fun findRoleByLibelleNot(libelle: String): List<Role>
    fun findRoleByIdIn(idList: List<Long>) : MutableList<Role>
 }