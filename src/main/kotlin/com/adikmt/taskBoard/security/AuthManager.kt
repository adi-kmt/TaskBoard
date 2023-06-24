package com.adikmt.taskBoard.security

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.repositories.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono
import java.security.Principal

class AuthManager @Autowired constructor(private val userRepository: UserRepository) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val principal = authentication?.principal as Principal

        val userResponse = userRepository
            .getUserByUserId(principal.name.toInt())

        return Mono.just(userResponse)
            .filter {
                (it == DbResponseWrapper.ServerException(Exception())) &&
                        (it == DbResponseWrapper.UserException(Exception())) &&
                        (it == DbResponseWrapper.DBException(Exception())) &&
                        (it == DbResponseWrapper.ServerException(Exception()))
            }.switchIfEmpty(Mono.empty())
            .flatMap {
                Mono.just((it as DbResponseWrapper.Success).data)
            }
            .map { user ->
                UsernamePasswordAuthenticationToken(user.userId, null)
            }
    }
}