package com.uptech.buanderie.services.implementations

import com.mailjet.client.errors.MailjetException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import com.uptech.buanderie.extentions.isValidEmail
import com.uptech.buanderie.models.dto.*
import com.uptech.buanderie.models.entity.*
import com.uptech.buanderie.repositories.ResetPasswordTokenRepository
import com.uptech.buanderie.repositories.SetPasswordTokenRepository
import com.uptech.buanderie.repositories.UserInfoRepository
import com.uptech.buanderie.repositories.UserRepository
import com.uptech.buanderie.security.SecurityUtils
import com.uptech.buanderie.security.UserDetailsImpl
import com.uptech.buanderie.services.IRoleService
import com.uptech.buanderie.services.IUserService
import com.uptech.buanderie.utilities.EmailUtils
import java.util.*
import java.util.function.Function

@Service
class UserService: IUserService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var userInfoRepository: UserInfoRepository

    @Autowired
    private lateinit var roleService: IRoleService

    private var passwordEncoder = BCryptPasswordEncoder()

    @Autowired
    private lateinit var  resetPasswordTokenRepository: ResetPasswordTokenRepository
    @Autowired
    private lateinit var  setPasswordTokenRepository: SetPasswordTokenRepository

    override fun init() {
        if (userRepository.findAll().isEmpty()) {
            addSuperAdmin()
        }
    }

    fun addSuperAdmin(): User {

        val superAdminFirstName = "Root"
        val superAdminLastName = "Admin"
        val superAdminUserName = "root"
        val superAdminEmail = "support@uptech.com.tn"
        val superAdminPassword = "azerty"

        val userInfo = UserInfo()
        userInfo.firstName = superAdminFirstName
        userInfo.lastName = superAdminLastName
        userInfo.userName = superAdminUserName
        userInfo.email = superAdminEmail

        val user = User()
        user.pseudo = superAdminUserName
        user.email = superAdminEmail
        user.roleList = roleService.findAll()
        user.archived = false
        user.enabled = true
        user.deletable = false
        user.pwd = passwordEncoder.encode(superAdminPassword)
        user.userInfo = userInfoRepository.save(userInfo)
        return userRepository.save(user)
    }

    override fun getUserProfil(): UserInfo {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val appUser = userRepository.findUserByEmail((authentication.principal as UserDetailsImpl).username).orElseThrow { RuntimeException ("unKnown User") }

        val user = findByUserName(appUser.email)
        return user.userInfo

    }

    override fun add(addUserDto: AddUserDto): Any {

        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val appUser = userRepository.findUserByEmail((authentication.principal as UserDetailsImpl).username).orElseThrow { RuntimeException ("unKnown User") }
        val userSession = findByUserName(appUser.email)
        val userRoleList = roleService.getRoleFromIdlist(addUserDto.roleIdList)
        if (userSession.roleList.find { it.libelle  == "SuperAdmin"} == null && userRoleList.find { it.libelle == "SuperAdmin" } != null){
            return 5003
        }
        if (isEmailUsed(addUserDto.email)){
            return 5001
        } else if (isUserNameUsed(addUserDto.pseudo)){
            return 5004
        } else {
            val userInfo = userInfoRepository.save(UserInfo(addUserDto))
            val user = userRepository.save(User(addUserDto, userInfo, userRoleList))
            val passwordSetToken  = createPasswordSetTokenForUser(user)
            return try {
                EmailUtils.sendSetPasswordEmail(
                    user.userInfo.email,
                    user.userInfo.firstName,
                    user.userInfo.lastName,
                    passwordSetToken.token
                )
                user
            } catch (e: MailjetException) {
                e.printStackTrace()
                5002
            }
        }

    }

    override fun edit( editUserDto: EditUserDto): Any {
        val user = findById(editUserDto.id)!!

        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val appUser = userRepository.findUserByEmail((authentication.principal as UserDetailsImpl).username).orElse(User())
        val userSession = findByUserName(appUser.email)!!
        val userRoleList = roleService.getRoleFromIdlist(editUserDto.roleIdList)

        if (userSession.roleList.find { it.libelle  == "SuperAdmin"} == null && userRoleList.find { it.libelle == "SuperAdmin" } != null){
            return 5003
        }
        return if (user.deletable){
            user.userInfo.firstName = editUserDto.firstName
            user.userInfo.lastName = editUserDto.lastName
            user.userInfo.userName = editUserDto.pseudo
            user.pseudo = editUserDto.pseudo
            user.roleList = roleService.getRoleFromIdlist(editUserDto.roleIdList)
            userRepository.save(user)
        } else {
            5001
        }

    }

    override fun enable(enableUserDto: EnableUserDto): Any {
        val user = findById(enableUserDto.id)!!
        return if (user.deletable){
            user.enabled = enableUserDto.enabled
            userRepository.save(user)
        } else {
            5001
        }
    }

    override fun archive(archiveUserDto: ArchiveUserDto): Any {
        val user = findById(archiveUserDto.id)!!
        return if (user.deletable){
            user.archived = archiveUserDto.archived
            userRepository.save(user)
        } else {
            5001
        }

    }

    override fun sendSetPasswordEmail(sendSetPassordEmailDto: SendSetPassordEmailDto):Int{
        val user = findById(sendSetPassordEmailDto.id)!!
        val setPasswordToken : PasswordSetToken? = setPasswordTokenRepository.findByUserId(user.id).orElse(null)
        if (user.pwd == null){
            if (setPasswordToken != null){
                setPasswordTokenRepository.deleteById(setPasswordToken.id)
            }
            val passwordSetToken = createPasswordSetTokenForUser(user)
            try {
                EmailUtils.sendSetPasswordEmail(
                    user.email,
                    user.userInfo.firstName,
                    user.userInfo.lastName,
                    passwordSetToken.token
                )
                return 200
            } catch (e: MailjetException) {
                e.printStackTrace()
                return 5002
            }
        }else {
            return 5001
        }
    }


    fun findById(idUser: Long): User? {
        return userRepository.findById(idUser).orElse(null)
    }

    override fun findAll(page: Int, perPage: Int): Page<User> {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val appUser = userRepository.findUserByEmail((authentication.principal as UserDetailsImpl).username).orElse(User())

        val user = findByUserName(appUser.email)!!

        val superAdminRole = roleService.findByLibelle("SuperAdmin")
        val adminRole = roleService.findByLibelle("Admin")

        return if (user.roleList.find { it.libelle == superAdminRole.libelle } != null) {
            userRepository.findAll( PageRequest.of(page, perPage))
        } else  {
            userRepository.findAllByRoleListNotContaining(superAdminRole ,  PageRequest.of(page, perPage))
        }
    }

    override fun findAllTechnicien(): List<User> {
        val technicienRole = roleService.findByLibelle("TechnicienSAV")
        return  userRepository.findAllByRoleListContainingAndArchived(technicienRole,false)
    }


    override fun findByUserName(userName: String): User {
        return if (userName.isValidEmail()) {
            findByEmail(userName)
        } else {
            findByPseudo(userName)
        }
    }


    override fun findByEmail(email: String): User {
        return userRepository.findUserByEmail(email).orElseThrow { RuntimeException("Unknown User") }
    }

    fun findByPseudo(email: String): User {
        return userRepository.findUserByPseudo(email).orElseThrow { RuntimeException("Unknown User") }
    }

    override fun isEmailUsed(email: String): Boolean {
        return userRepository.findUserByEmail(email).isPresent
    }

    override fun isUserNameUsed(userName: String): Boolean {
        return userRepository.findUserByPseudo(userName).isPresent
    }

    override fun isPasswordinitilized(userName: String): Boolean {
        val user = findByUserName(userName)
        return if (user != null){
            user.pwd != null
        } else {
            true
        }
    }

    override fun isEnabled(userName: String): Boolean {
        val user = findByUserName(userName)
        return if (user != null){
            return user.enabled
        } else {
            false
        }
    }

    override fun isArchived(userName: String): Boolean {
        val user = findByUserName(userName)
        return if (user != null){
            return user.archived
        } else {
            false
        }
    }

    override fun register(registerDto: RegisterDto): User {
        val userInfo = userInfoRepository.save(UserInfo(registerDto))
        val user = User(registerDto,userInfo)
        user.pwd =passwordEncoder.encode(user.pwd)
        return userRepository.save<User>(user)
    }

    override fun changePassword(resetPasswordDto: ResetPasswordDto) {
        val passwordResetToken: PasswordResetToken = resetPasswordTokenRepository.findByToken(resetPasswordDto.token).orElse(null)
        val user: User = passwordResetToken.user
        user.pwd = passwordEncoder.encode(resetPasswordDto.newPassword)
        userRepository.save(user)
        resetPasswordTokenRepository.deleteById(passwordResetToken.id)
    }

    override fun createPasswordResetTokenForUser(user: User) : PasswordResetToken {
        val token: String = UUID.randomUUID().toString()
        val myToken = PasswordResetToken(token, user)
        return resetPasswordTokenRepository.save<PasswordResetToken>(myToken)
    }

    override fun validatePasswordResetToken(token: String): Int {
        val passToken: PasswordResetToken? = resetPasswordTokenRepository.findByToken(token).orElse(null)
        return if (!isResetPasswordTokenFound(passToken)) 5001 else if (isResetPasswordTokenExpired(passToken)) 5002 else 200
    }

    private fun isResetPasswordTokenFound(passToken: PasswordResetToken?): Boolean {
        return passToken != null
    }

    private fun isResetPasswordTokenExpired(passToken: PasswordResetToken?): Boolean {
        val cal: Calendar = Calendar.getInstance()
        return passToken?.createdAt!!.time + SecurityUtils.RESET_PWD_TOKEN_EXPIRATION<= cal.timeInMillis
    }

    override fun deleteResetPasswordTokenExpiredTokens() {
        var dt = Date()
        val c: Calendar = Calendar.getInstance()
        c.time = dt
        c.add(Calendar.MILLISECOND, -SecurityUtils.RESET_PWD_TOKEN_EXPIRATION)
        dt = c.time
        resetPasswordTokenRepository.deleteByCreatedAtBefore(dt)
    }

    override fun setPassword(setPasswordDto: SetPasswordDto) {
        val passwordSetToken: PasswordSetToken = setPasswordTokenRepository.findByToken(setPasswordDto.token).orElse(null)
        val user: User = passwordSetToken.user
        user.pwd = passwordEncoder.encode(setPasswordDto.password)
        userRepository.save(user)
        setPasswordTokenRepository.deleteById(passwordSetToken.id)
    }

    fun createPasswordSetTokenForUser(user: User) :PasswordSetToken{
        println("tokenPwd:::::${user.email}")
        val token: String = UUID.randomUUID().toString()
        println("tokenPwd:::::${token}")
        val myToken = PasswordSetToken(token, user)
        println("tokenPwd::::::::::::::::::::::::::::::::::::::::::::::")
        println("tokenPwd:::::${myToken.token}")
        println("tokenPwd:::::${myToken.user.id}")
        println("tokenPwd:::::${myToken.user.email}")
        val pwdToken = setPasswordTokenRepository.save(myToken)
        println("tokenPwd:::::${pwdToken.token}")
        return myToken
    }

    override fun validatePasswordSetToken(token: String): Int {
        val passToken: PasswordSetToken? = setPasswordTokenRepository.findByToken(token).orElse(null)
        return if (!isSetPasswordTokenFound(passToken)) 5001 else if (isSetPasswordTokenTokenExpired(passToken)) 5002 else 200
    }

    private fun isSetPasswordTokenFound(passToken: PasswordSetToken?): Boolean {
        return passToken != null
    }

    private fun isSetPasswordTokenTokenExpired(passToken: PasswordSetToken?): Boolean {
        val cal: Calendar = Calendar.getInstance()

        return passToken?.createdAt!!.time + SecurityUtils.SET_PWD_TOKEN_EXPIRATION<= cal.timeInMillis
    }

    override fun deleteSetPasswordTokenExpiredTokens() {
        var dt = Date()
        val c: Calendar = Calendar.getInstance()
        c.time = dt
        c.add(Calendar.MILLISECOND, -SecurityUtils.SET_PWD_TOKEN_EXPIRATION)
        dt = c.time
        setPasswordTokenRepository.deleteByCreatedAtBefore(dt)
    }


}