package com.uptech.buanderie.services.implementations


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.uptech.buanderie.models.entity.Role
import com.uptech.buanderie.repositories.RoleRepository
import com.uptech.buanderie.services.IRoleService
import java.util.ArrayList

@Service
class RoleService : IRoleService {

    @Autowired
    private lateinit var roleRepository: RoleRepository

    override fun init(): MutableList<Role> {
        val result: MutableList<Role> = ArrayList<Role>()
        if (findAll().isEmpty()) {
            result.add(addRole(Role("Admin")))
            result.add(addRole(Role("SuperAdmin")))
        }
        return result
    }

    override fun addRole(role: Role): Role {
        return roleRepository.save(role)
    }

    override fun findAll(): MutableList<Role> {
        return roleRepository.findAll()
    }
    override fun findByLibelle(libelle:String): Role {
        return roleRepository.findRoleByLibelle(libelle)
    }

    override fun findWithoutSuperAdmin(): List<Role> {
        return roleRepository.findRoleByLibelleNot("SuperAdmin")
    }

    override fun getRoleFromIdlist(idList: List<Long>) : MutableList<Role>{
        return roleRepository.findRoleByIdIn(idList)
    }
}