package com.uptech.buanderie.controllers


import com.uptech.buanderie.models.entity.Role
import com.uptech.buanderie.models.response.RequestResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.uptech.buanderie.services.IRoleService

@RestController
@RequestMapping(value = ["/api/v1/role"])
@CrossOrigin

class RoleContorller {
    @Autowired
    private lateinit var roleService: IRoleService

    @GetMapping(value = ["/findAll"])
    fun findAll(): ResponseEntity<RequestResponse<Any>> {
        val result: List<Role> = roleService.findAll()
        return ResponseEntity.ok(RequestResponse(200, "Success", result))
    }
}