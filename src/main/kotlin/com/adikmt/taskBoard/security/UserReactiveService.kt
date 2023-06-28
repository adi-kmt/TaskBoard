package com.adikmt.taskBoard.security

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.repositories.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserReactiveService @Autowired constructor(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        return Mono.fromCallable { userRepository.getUserByUserId(username.toInt()) }
            .flatMap { userResponse ->
                when (userResponse) {
                    is DbResponseWrapper.Success -> Mono.just(userResponse.data)
                    else -> Mono.error(Exception("User not found"))
                }
            }
    }
}