package com.adikmt.taskBoard.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun springSecurityFilterChain(
        converter: JwtServerAuthenticationConverter,
        http: ServerHttpSecurity,
        authManager: JwtAuthenticationManager,
        corsConfigurationSource: CorsConfigurationSource,
        jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
        jwtAccessDeniedHandler: JwtAccessDeniedHandler
    ): SecurityWebFilterChain {

        val filter = AuthenticationWebFilter(authManager)
        filter.setServerAuthenticationConverter(converter)

        return http
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
            }
            .authorizeExchange {
                it.pathMatchers(
                    HttpMethod.GET,
                    "/webjars/swagger-ui/index.html",
                    "/swagger-ui.html",
                    "/webjars/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/register", "/api/login").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/refreshToken").permitAll()
                    .pathMatchers("/**").authenticated()
            }
            .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
            .httpBasic {
                it.disable()
            }
            .formLogin {
                it.disable()
            }
            .csrf {
                it.disable()
            }
            .cors {
                it.configurationSource(corsConfigurationSource)
            }
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        val source = UrlBasedCorsConfigurationSource()
        config.allowedOrigins = listOf(
            "*"
        )
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return source
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