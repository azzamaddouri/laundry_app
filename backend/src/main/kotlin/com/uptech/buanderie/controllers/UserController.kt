package com.uptech.buanderie.controllers


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.uptech.buanderie.Log
import com.uptech.buanderie.models.dto.*
import com.uptech.buanderie.models.entity.User
import com.uptech.buanderie.models.response.RequestResponse
import com.uptech.buanderie.services.IUserService

@RestController
@RequestMapping(value = ["/api/v1/user"])
@CrossOrigin
class UserController {

    @Autowired
    private lateinit var userService: IUserService

    @PostMapping(value = ["/add"])
    fun add(@RequestBody addUserDto: AddUserDto
    ): ResponseEntity<RequestResponse<Any>> {

        return when (userService.add(addUserDto)) {
            is User -> ResponseEntity.ok(RequestResponse(200, "Success", null))
            5001 -> ResponseEntity.ok(RequestResponse(5001, "email used", null))
            5002 -> ResponseEntity.ok(RequestResponse(5002, "Email not sent", null))
            5003 -> ResponseEntity.ok(RequestResponse(5003, "User cannot add a SuperAdmin", null))
            5003 -> ResponseEntity.ok(RequestResponse(5004, "UserName Used", null))
            else -> ResponseEntity.ok(RequestResponse(5099, "Erreur inconnue", null))
        }
    }

    @PutMapping(value = ["/edit"])
    fun edit(
        @RequestBody editUserDto: EditUserDto
    ): ResponseEntity<RequestResponse<Any>> {

        return when (val result = userService.edit( editUserDto)) {
            is User -> ResponseEntity.ok(RequestResponse<Any>(200, "Success", result))
            5001 -> ResponseEntity.ok(RequestResponse<Any>(5001, "User cannot be modified", result))
            5003 -> ResponseEntity.ok(RequestResponse<Any>(5003, "User cannot modify a SuperAdmin", result))
            else -> ResponseEntity.ok(RequestResponse<Any>(5099, "Erreur inconnue", result))
        }
    }


    @GetMapping(value = ["/findAll"])
    fun findAll(@RequestParam(name = "page", defaultValue = "0", required = false)  page : Int,
                @RequestParam(name = "perPage", defaultValue = "15", required = false) perPage : Int)
    :ResponseEntity<RequestResponse<Page<User>>> {


        val result = userService.findAll(page, perPage)
        return ResponseEntity.ok(RequestResponse(200, "Success", result))
    }


    @PutMapping(value = ["/archive"])
    fun archive(@RequestBody archiveUserDto: ArchiveUserDto): ResponseEntity<RequestResponse<Any>> {
        Log.i("archiveValue::::${archiveUserDto.id}::::${archiveUserDto.archived}")
        val result = userService.archive(archiveUserDto)
        return if (result is User) {
            ResponseEntity.ok(RequestResponse<Any>(200, "Success", result))
        } else {
            if (result == 5001) {
                ResponseEntity.ok(RequestResponse<Any>(5001, "User cannot be modified", result))
            } else {
                ResponseEntity.ok(RequestResponse<Any>(5099, "Erreur inconnue", result))
            }
        }
    }

    @PostMapping(value = ["/sendSetPasswordEmail"])
    fun sendSetPasswordEmail(@RequestBody sendSetPassordEmailDto: SendSetPassordEmailDto): ResponseEntity<RequestResponse<Any>> {

        val result = userService.sendSetPasswordEmail(sendSetPassordEmailDto)
        when (result) {
            200 -> return ResponseEntity.ok(RequestResponse<Any>(200, "Success", null))
            5001 -> return ResponseEntity.ok(RequestResponse<Any>(5001, "Password initilized", null))
            5002 -> return ResponseEntity.ok(RequestResponse<Any>(5002, "Email not sent", null))
            else -> return ResponseEntity.ok(RequestResponse<Any>(5099, "Unknown Error", null))
        }

    }

    @PutMapping(value = ["/enable"])
    fun enable(@RequestBody enableUserDto: EnableUserDto): ResponseEntity<RequestResponse<Any>> {

        val result = userService.enable(enableUserDto)
        return if (result is User) {
            ResponseEntity.ok(RequestResponse<Any>(200, "Success", result))
        } else {
            if (result == 5001) {
                ResponseEntity.ok(RequestResponse<Any>(5001, "User cannot be modified", result))
            } else {
                ResponseEntity.ok(RequestResponse<Any>(5099, "Erreur inconnue", result))
            }
        }
    }
}