package com.uptech.buanderie.security


import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl :UserDetails {

    private val serialVersionUID = 1L

    private var id: Long = 0

    private var username: String = ""

    @JsonIgnore
    private var password: String? = null

    private var authorities: Collection<GrantedAuthority> = listOf()

    constructor(id: Long, username: String, password: String?, authorities: Collection<GrantedAuthority>) {
        this.id = id
        this.username = username
        this.password = password
        this.authorities = authorities
    }


    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val user = o as UserDetailsImpl
        return id == user.id
    }

}