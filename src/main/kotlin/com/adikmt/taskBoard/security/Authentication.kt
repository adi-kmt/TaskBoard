package com.adikmt.taskBoard.security

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager(
    private val jwtSupport: JwtSupport,
    private val users: UserReactiveService
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        return Mono.justOrEmpty(authentication)
            .filter { auth -> auth is BearerToken }
            .cast(BearerToken::class.java)
            .flatMap { jwt -> mono { validate(jwt) } }
            .onErrorMap { error -> Exception(error.message) }
    }

    private suspend fun validate(token: BearerToken): Authentication {
        try {
            val username = jwtSupport.getUsername(token)
            val user = users.findByUsername(username).awaitSingle()

            if (jwtSupport.isValid(token, user)) {
                return UsernamePasswordAuthenticationToken(user.username, user.password, user.authorities)
            }
            throw Exception("Token is invalid")
        } catch (e: Exception) {

            throw Exception(e.message)
        }

    }

}
