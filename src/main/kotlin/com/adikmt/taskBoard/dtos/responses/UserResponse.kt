package com.adikmt.taskBoard.dtos.responses

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserResponse(
    val userId: Int?,
    val userName: String?,
    val userPassword: String?,
    val jwtToken: String
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String =
        this.userPassword

    override fun getUsername(): String =
        this.userId.toString()

    override fun isAccountNonExpired(): Boolean =
        true

    override fun isAccountNonLocked(): Boolean =
        true

    override fun isCredentialsNonExpired(): Boolean =
        true

    override fun isEnabled(): Boolean =
        true
}
