package com.uptech.buanderie.models.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import com.uptech.buanderie.models.dto.AddUserDto
import com.uptech.buanderie.models.dto.RegisterDto
import javax.persistence.*

@Entity
@Table(name = "UserInfo")
@EntityListeners(AuditingEntityListener::class)
@Audited

class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
    var firstName: String = ""
    var lastName: String = ""
    var userName: String = ""
    var email: String = ""

    @JsonIgnore
    @OneToOne(mappedBy = "userInfo")
    lateinit var user: User

    constructor(){}
    constructor(registerDto: RegisterDto) {
        this.firstName = registerDto.firstName
        this.lastName = registerDto.lastName
        this.userName = registerDto.pseudo
        this.email = registerDto.email
    }

    constructor(addUserDto: AddUserDto) {
        firstName = addUserDto.firstName
        lastName = addUserDto.lastName
        userName = addUserDto.pseudo
        email = addUserDto.email
    }
}