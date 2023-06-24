package com.adikmt.taskBoard.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
@EnableReactiveMethodSecurity
class WebSecurityConfig @Autowired constructor(
    private val jwtUtil: JWTUtil,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) {
    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        corsConfigurationSource: CorsConfigurationSource,
        authManager: AuthManager
    ): SecurityWebFilterChain {
        return http
            .authorizeExchange {
                it.pathMatchers(
                    HttpMethod.GET,
                    "/webjars/swagger-ui/index.html",
                    "/swagger-ui.html",
                    "/webjars/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/register", "/api/login").permitAll()
                    .pathMatchers("/**").authenticated()
            }
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
            }
            .addFilterAt(bearerAuthenticationFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
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

    /**
     * Spring security works by filter chaining.
     * We need to add a JWT CUSTOM FILTER to the chain.
     *
     * what is AuthenticationWebFilter:
     *
     *  A WebFilter that performs authentication of a particular request. An outline of the logic:
     *  A request comes in and if it does not match setRequiresAuthenticationMatcher(ServerWebExchangeMatcher),
     *  then this filter does nothing and the WebFilterChain is continued.
     *  If it does match then... An attempt to convert the ServerWebExchange into an Authentication is made.
     *  If the result is empty, then the filter does nothing more and the WebFilterChain is continued.
     *  If it does create an Authentication...
     *  The ReactiveAuthenticationManager specified in AuthenticationWebFilter(ReactiveAuthenticationManager) is used to perform authentication.
     *  If authentication is successful, ServerAuthenticationSuccessHandler is invoked and the authentication is set on ReactiveSecurityContextHolder,
     *  else ServerAuthenticationFailureHandler is invoked
     *
     */

    fun bearerAuthenticationFilter(authManager: AuthManager): AuthenticationWebFilter {
        val bearerAuthentication = AuthenticationWebFilter(authManager)
        bearerAuthentication.setAuthenticationConverter(HttpAuthenticationConverter(jwtUtil))
        bearerAuthentication.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"))
        bearerAuthentication.setAuthenticationSuccessHandler { _, authentication ->
            ReactiveSecurityContextHolder.getContext().share().block()?.authentication = authentication
            Mono.empty()
        }

        return bearerAuthentication
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