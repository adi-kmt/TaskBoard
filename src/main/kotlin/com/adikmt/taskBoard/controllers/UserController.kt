package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.ResponseStatus
import com.adikmt.taskBoard.dtos.common.wrappers.ResponseWrapper
import com.adikmt.taskBoard.dtos.common.wrappers.unwrap
import com.adikmt.taskBoard.dtos.requests.LoginUserRequest
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.JWTUserResponse
import com.adikmt.taskBoard.dtos.responses.UserResponse
import com.adikmt.taskBoard.services.users.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController @Autowired constructor(private val userService: UserService) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody userRequest: LoginUserRequest)
            : ResponseEntity<ResponseWrapper<UserResponse>> {
        return try {
            userService.login(userRequest).unwrap()
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody userRequest: UserRequest): ResponseEntity<ResponseWrapper<JWTUserResponse>> {
        return try {
            userService.registerUser(userRequest).unwrap(successResponseStatus = ResponseStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/logout")
    fun logout(@RequestHeader header: HttpHeaders): ResponseEntity<ResponseWrapper<String>> {
        val authHeader = header.getFirst(HttpHeaders.AUTHORIZATION)
        if (authHeader.isNullOrEmpty() || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity(
                ResponseWrapper(errorMessage = "No authorization header found"),
                HttpStatus.UNAUTHORIZED
            )
        }

        ReactiveSecurityContextHolder.clearContext()

        return ResponseEntity(
            ResponseWrapper(data = "Logout process completed"), HttpStatus.OK
        )
    }
}