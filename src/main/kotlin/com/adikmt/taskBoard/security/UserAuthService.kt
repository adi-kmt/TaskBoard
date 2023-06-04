package com.adikmt.taskBoard.security

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.repositories.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

interface UserAuthService : UserDetailsService

@Service
class UserAuthServiceImpl @Autowired constructor(
    private val userRepository: UserRepository
) : UserAuthService {
    override fun loadUserByUsername(username: String?): UserDetails {
        username?.let {
            when (val user = userRepository.getUserByUserName(userId = it.toInt())) {
                is DbResponseWrapper.Success -> {
                    return user.data ?: run {
                        throw UsernameNotFoundException("User not found")
                    }
                }
                else -> throw UsernameNotFoundException("User not found")
            }
        } ?: run {
            throw UsernameNotFoundException("User not found")
        }
    }
}