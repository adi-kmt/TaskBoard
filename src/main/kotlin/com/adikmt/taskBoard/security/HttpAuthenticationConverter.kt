package com.adikmt.taskBoard.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.function.Function
import java.util.function.Predicate

class HttpAuthenticationConverter @Autowired constructor(private val jwtUtil: JWTUtil) :
        (ServerWebExchange) -> Mono<Authentication> {
    private val BEARER = "Bearer "
    private val matchBearerLength =
        Predicate { authValue: String -> authValue.length > BEARER.length }
    private val isolateBearerValue =
        Function { authValue: String ->
            Mono.justOrEmpty(
                authValue.substring(BEARER.length)
            )
        }

    override fun invoke(serverWebExchange: ServerWebExchange): Mono<Authentication> =
        Mono.justOrEmpty(serverWebExchange)
            .flatMap(::extract)
            .filter(matchBearerLength)
            .flatMap(isolateBearerValue)
            .flatMap(jwtUtil::validateTokenAndReturnAuthentication)

    private fun extract(serverWebExchange: ServerWebExchange): Mono<String> =
        Mono.justOrEmpty(
            serverWebExchange.request
                .headers
                .getFirst(HttpHeaders.AUTHORIZATION)
        )
}