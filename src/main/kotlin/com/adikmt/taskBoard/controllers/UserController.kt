package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.ResponseStatus
import com.adikmt.taskBoard.dtos.common.ResponseWrapper
import com.adikmt.taskBoard.dtos.common.unwrap
import com.adikmt.taskBoard.dtos.requests.LoginUserRequest
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.UserResponse
import com.adikmt.taskBoard.services.users.UserService
import jakarta.validation.Valid
import kotlinx.coroutines.flow.StateFlow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class UserController @Autowired constructor(private val userService: UserService) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody userRequest: LoginUserRequest)
            : Mono<ResponseEntity<ResponseWrapper<UserResponse>>> {
        return try {
            Mono.just(
                userService.login(userRequest).unwrap()
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }

    @PostMapping("/register")
    fun register(@Valid @RequestBody userRequest: UserRequest): Mono<ResponseEntity<ResponseWrapper<Int?>>> {
        return try {
            Mono.just(
                userService.registerUser(userRequest).unwrap(responseStatus = ResponseStatus.CREATED)
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }

}