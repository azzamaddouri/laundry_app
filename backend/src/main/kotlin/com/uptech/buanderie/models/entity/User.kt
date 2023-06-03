package com.uptech.buanderie.models.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Fetch
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import com.uptech.buanderie.models.dto.AddUserDto
import com.uptech.buanderie.models.dto.RegisterDto
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "User")
@EntityListeners(AuditingEntityListener::class)
@Audited

class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    @Column(unique = true)
    var pseudo: String = ""
    @Column(unique = true)
    var email: String = ""

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var pwd: String? = null

    @ManyToMany
    @Fetch(org.hibernate.annotations.FetchMode.JOIN)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "userId", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "roleId", referencedColumnName = "id")]
    )

    var roleList: MutableList<Role> = mutableListOf()

    var enabled = false
    var archived = false
    var deletable = true

    @OneToOne
    @JoinColumn(name = "idUserInfo")
    var userInfo = UserInfo()

    @JsonIgnore
    @OneToOne
    @CreatedBy
    var createdBy: User? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    var createdAt: Date? = null

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @LastModifiedDate
    var updatedAt: Date? = null


    constructor() {}
    constructor(registerDto: RegisterDto,userInfo: UserInfo) {
        this.pseudo = registerDto.pseudo
        this.email = registerDto.email
        this.pwd = registerDto.pwd
        this.enabled = false
        this.archived = false
        this.deletable = true
        this.userInfo = userInfo
    }

    constructor(addUserDto: AddUserDto, userInfo: UserInfo, roleList: List<Role>) {
        pseudo = addUserDto.pseudo
        email = addUserDto.email
        pwd = null
        this.roleList.clear()
        this.roleList.addAll(roleList)
        this.enabled = true
        this.archived = false
        this.deletable = true

        this.userInfo = userInfo
    }
}