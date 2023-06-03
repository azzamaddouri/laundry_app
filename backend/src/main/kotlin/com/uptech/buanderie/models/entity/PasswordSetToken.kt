package com.uptech.buanderie.models.entity

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*

@Entity
@EntityListeners(AuditingEntityListener::class)
class PasswordSetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0
    var token: String = ""

    @OneToOne
    lateinit var user: User

    @CreatedDate
    lateinit var createdAt: Date

    constructor() {}
    constructor(token: String, user: User) {
        this.token = token
        this.user = user
    }

}