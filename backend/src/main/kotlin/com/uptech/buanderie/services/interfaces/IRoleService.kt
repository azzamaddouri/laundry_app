package com.uptech.buanderie.services

import com.uptech.buanderie.models.entity.Role


interface IRoleService {
    fun init(): MutableList<Role>
    fun addRole(role: Role): Role
    fun findAll(): MutableList<Role>
    fun findByLibelle(libelle: String): Role
    fun findWithoutSuperAdmin(): List<Role>
    fun getRoleFromIdlist(idList: List<Long>): MutableList<Role>
}