package com.uptech.buanderie.controllers


import com.uptech.buanderie.models.response.RequestResponse
import com.uptech.buanderie.security.SecurityUtils
import com.mailjet.client.errors.MailjetException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import com.uptech.buanderie.utilities.EmailUtils
import com.uptech.buanderie.models.dto.*
import com.uptech.buanderie.models.entity.User
import com.uptech.buanderie.services.IUserService

@RestController
@RequestMapping(value = ["/api/v1/auth"])
@CrossOrigin
class AuthController {

    @Autowired
    private lateinit var userService: IUserService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @PostMapping(value = ["/signIn"])
    fun signIn(@RequestBody loginDto: LoginDto): ResponseEntity<RequestResponse<Any>> {
        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginDto.username, loginDto.password)
            )
        } catch (e: BadCredentialsException) {

            return when {

                userService.findByUserName(loginDto.username) == null->{ResponseEntity.badRequest().body( RequestResponse(301, "Error username or passowrd", null))}
                !userService.isPasswordinitilized(loginDto.username)->{ResponseEntity.badRequest().body( RequestResponse(5001, "Password not initilized", null))}
                !userService.isEnabled(loginDto.username)->{ResponseEntity.badRequest().body( RequestResponse(5002, "User Disabled", null))}
                userService.isArchived(loginDto.username)->{ResponseEntity.badRequest().body( RequestResponse(5003, "User Archived", null))}
                else->{ResponseEntity.badRequest().body( RequestResponse(301, "Error username or passowrd", null))}

            }
        }

        SecurityContextHolder.getContext().authentication = authentication
        val jwt: String = SecurityUtils.generateJwtToken(authentication)
        return ResponseEntity.ok(RequestResponse(200, "Success", jwt))
    }

    @PostMapping(value = ["/register"])
    fun register(@RequestBody registerDto: RegisterDto): ResponseEntity<RequestResponse<Any>> {
        return if (userService.isEmailUsed(registerDto.email)) {
            ResponseEntity.ok(RequestResponse(301, "email used", null))
        } else if (userService.isUserNameUsed(registerDto.pseudo)) {
            ResponseEntity.ok(RequestResponse(301, "email used", null))
        } else {
            val result: User = userService.register(registerDto)
            ResponseEntity.ok(RequestResponse(200, "Success", result))
        }
    }

    @PostMapping(value = ["/forgotPassword"])
    fun forgotPassword(@RequestBody forgotPasswordDto: ForgotPasswordDto): ResponseEntity<RequestResponse<Any>> {

        val user: User = userService.findByEmail(forgotPasswordDto.email)
            ?: return ResponseEntity.ok(RequestResponse(5001, "unknown email", null))

        val resetToken = userService.createPasswordResetTokenForUser(user)

        try {
            EmailUtils.sendResetPasswordEmail(
                forgotPasswordDto.email,
                user.userInfo.firstName,
                user.userInfo.lastName,
                resetToken.token
            )
        } catch (e: MailjetException) {
            e.printStackTrace()
            return ResponseEntity.ok(RequestResponse(5002, "email not sended", null))
        }
        return ResponseEntity.ok(RequestResponse(200, "email sended", null))
    }

    @PostMapping("/resetPassword")
    fun resetPassword(@RequestBody resetPassworDto: ResetPasswordDto): ResponseEntity<RequestResponse<Any>> {
        val result: Int = userService.validatePasswordResetToken(resetPassworDto.token)
        return when (result) {
            200 -> {
                userService.changePassword(resetPassworDto)
                ResponseEntity.ok(RequestResponse(200, "password changed", null))
            }
            5001 -> ResponseEntity.ok(RequestResponse(5001, "unauthorized Token", null))
            5002 -> ResponseEntity.ok(RequestResponse(5002, "expired", null))
            else -> ResponseEntity.ok(RequestResponse(5003, "unknown error", null))
        }
    }

    @PostMapping("/setPassword")
    fun setPassword(@RequestBody setPassworDto: SetPasswordDto): ResponseEntity<RequestResponse<Any>> {
        val result: Int = userService.validatePasswordSetToken(setPassworDto.token)
        return when (result) {
            200 -> {
                userService.setPassword(setPassworDto)
                ResponseEntity.ok(RequestResponse(200, "Mot de passe créer", null))
            }
            5001 -> ResponseEntity.ok(RequestResponse(5001, "unauthorized Token", null))
            5002 -> ResponseEntity.ok(RequestResponse(5002, "expired", null))
            else -> ResponseEntity.ok(RequestResponse(50099, "unknown error", null))
        }
    }
}