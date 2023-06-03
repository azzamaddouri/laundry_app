package com.uptech.buanderie.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.uptech.buanderie.models.response.InitDataResponse
import com.uptech.buanderie.models.response.RequestResponse
import com.uptech.buanderie.repositories.*
import com.uptech.buanderie.services.IRoleService
import com.uptech.buanderie.services.IUserService


@RestController
@RequestMapping(value = ["/api/v1/init"])
@CrossOrigin
class InitDataController {
    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var roleService: IRoleService


    @GetMapping(value = ["/initData"])
    fun initData(): ResponseEntity<RequestResponse<Any>> {
        val response = InitDataResponse()

        response.userInfo = userService.getUserProfil()
        response.roleList = roleService.findAll()

        return ResponseEntity.ok(RequestResponse(200, "Success", response))
    }


}