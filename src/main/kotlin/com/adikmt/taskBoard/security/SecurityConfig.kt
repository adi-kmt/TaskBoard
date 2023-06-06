package com.adikmt.taskBoard.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Configuration
@EnableReactiveMethodSecurity
class SecurityConfig @Autowired constructor(
    private val userAuthService: UserAuthService,
    private val jwtAuthFilter: JWTAuthFilter,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {
    @Bean
    fun springWebFilterChain(
        http: ServerHttpSecurity,
        jwtUtil: JWTUtil,
        reactiveAuthenticationManager: ReactiveAuthenticationManager,
        corsConfigurationSource: CorsConfigurationSource
    ): SecurityWebFilterChain? {
        return http
            .authorizeExchange(Customizer {
                it
                    .pathMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**"
                    ).permitAll()
                    .pathMatchers("/register", "/login").permitAll()
                    .pathMatchers("/**").authenticated()
            })
            .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHORIZATION)
            .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.HTTP_BASIC)
            .csrf {
                it.disable()
            }
            .cors {
                it.configurationSource(corsConfigurationSource)
            }
            .formLogin {
                it.disable()
            }
            .httpBasic {
                it.disable()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                it.accessDeniedHandler(jwtAccessDeniedHandler)
            }.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        val source = UrlBasedCorsConfigurationSource()
        config.allowedOrigins = listOf(
            "http://localhost:8080",
            "http://localhost:3000"
        )
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return source
    }


    @Bean
    fun userDetailsService(): ReactiveUserDetailsService? {
        return ReactiveUserDetailsService { username: String? ->
            try {
                Mono.just(
                    userAuthService.loadUserByUsername(username)
                )
            } catch (e: Exception) {
                Mono.error(e)
            }
        }
    }

    @Bean
    fun reactiveAuthenticationManager(
        userAuthService: UserAuthService,
        passwordEncoder: PasswordEncoder
    ): ReactiveAuthenticationManager {
        val manager = UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService())
        manager.setPasswordEncoder(passwordEncoder)
        return manager
    }

}

@Component
class JwtAuthenticationEntryPoint : ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> {
        return if (ex is AuthenticationCredentialsNotFoundException) {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            exchange.response.writeWith(Mono.empty())
        } else {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            exchange.response.writeWith(Mono.empty())
        }
    }
}

@Component
class JwtAccessDeniedHandler : ServerAccessDeniedHandler {
    override fun handle(exchange: ServerWebExchange?, denied: AccessDeniedException?): Mono<Void>? {
        return if (denied is AccessDeniedException) {
            exchange?.response?.statusCode = HttpStatus.FORBIDDEN
            exchange?.response?.writeWith(Mono.empty())
        } else {
            exchange?.response?.statusCode = HttpStatus.I_AM_A_TEAPOT
            exchange?.response?.writeWith(Mono.empty())
        }
    }
}