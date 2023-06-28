package com.adikmt.taskBoard.dtos.responses

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserResponse(
    val userId: Int?,
    val userName: String?,
    val userPassword: String?
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String? =
        this.userPassword

    override fun getUsername(): String =
        this.userId.toString()

    override fun isAccountNonExpired(): Boolean =
        false

    override fun isAccountNonLocked(): Boolean =
        false

    override fun isCredentialsNonExpired(): Boolean =
        false

    override fun isEnabled(): Boolean =
        true
}
