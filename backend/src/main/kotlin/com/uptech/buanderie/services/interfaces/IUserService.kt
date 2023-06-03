package com.uptech.buanderie.services

import org.springframework.data.domain.Page
import com.uptech.buanderie.models.dto.*
import com.uptech.buanderie.models.entity.PasswordResetToken
import com.uptech.buanderie.models.entity.User
import com.uptech.buanderie.models.entity.UserInfo


interface IUserService {
    fun init()
    fun getUserProfil(): UserInfo
    fun add(addUserDto: AddUserDto): Any
    fun edit(editUserDto: EditUserDto): Any
    fun enable(enableUserDto: EnableUserDto): Any
    fun archive(archiveUserDto: ArchiveUserDto): Any
    fun sendSetPasswordEmail(sendSetPassordEmailDto: SendSetPassordEmailDto): Int
    fun findAll(page: Int, perPage: Int): Page<User>
    fun findAllTechnicien(): List<User>
    fun findByUserName(userName: String): User?
    fun findByEmail(email: String): User?
    fun isEmailUsed(email: String): Boolean
    fun isUserNameUsed(userName: String): Boolean
    fun isPasswordinitilized(userName: String): Boolean
    fun isEnabled(userName: String): Boolean
    fun isArchived(userName: String): Boolean
    fun register(registerDto: RegisterDto): User
    fun changePassword(resetPasswordDto: ResetPasswordDto)
    fun createPasswordResetTokenForUser(user: User) : PasswordResetToken
    fun validatePasswordResetToken(token: String): Int
    fun deleteResetPasswordTokenExpiredTokens()
    fun setPassword(setPasswordDto: SetPasswordDto)
    fun validatePasswordSetToken(token: String): Int
    fun deleteSetPasswordTokenExpiredTokens()
}