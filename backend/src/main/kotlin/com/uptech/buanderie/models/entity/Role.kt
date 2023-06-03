package com.uptech.buanderie.models.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.envers.Audited
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
@Table(name = "Role")
@EntityListeners(AuditingEntityListener::class)
@Audited

class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @Column(nullable = false, unique = true)
    var libelle: String = ""

    @JsonIgnore
    @ManyToMany(mappedBy = "roleList")
    val userList: List<User> = listOf()

    constructor() {}
    constructor(libelle: String) {
        this.libelle = libelle
    }
}