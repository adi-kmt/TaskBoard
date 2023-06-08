package com.adikmt.taskBoard.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JWTAuthFilter @Autowired constructor(
    private val jwtUtil: JWTUtil,
    private val userDetailsService: UserAuthService
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        try {
            val request = exchange.request
            val authorizationHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                val token = authorizationHeader.substring(7)

                if (jwtUtil.validateToken(token)) {
                    val claims = jwtUtil.extractClaims(token)
                    val userId = jwtUtil.getUserIdFromToken(token)
                    val user = userDetailsService.loadUserByUsername(userId)
                    val authorities = claims.get("authorities", List::class.java)
                        .map { SimpleGrantedAuthority(it.toString()) }
                    val authentication = UsernamePasswordAuthenticationToken(
                        user.username,
                        null,
                        authorities
                    )
                    ReactiveSecurityContextHolder.withAuthentication(authentication)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return chain.filter(exchange)
    }
}