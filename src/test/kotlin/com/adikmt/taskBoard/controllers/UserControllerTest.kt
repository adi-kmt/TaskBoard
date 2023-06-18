package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LoginUserRequest
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.UserResponse
import com.adikmt.taskBoard.services.users.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import reactor.test.StepVerifier

@SpringBootTest
class UserControllerTest {

    private val mockUserService: UserService = mockk<UserService>()

    private val userController: UserController = UserController(mockUserService)

    private val loginUserRequest = LoginUserRequest(userID = 0, userName = "Namessa", password = "Password")

    private val userRequest = UserRequest(userName = "Namessa", password = "Password")

    private val userResponse = UserResponse(
        userId = 0,
        userName = "Namessa",
        userPassword = "Password",
    )

    @Test
    fun `check login success`() {
        every { mockUserService.login(loginUserRequest = loginUserRequest) }.returns(
            DbResponseWrapper.Success(data = userResponse)
        )

        val response = userController.login(loginUserRequest)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.OK)
            assert(responseEntity.body?.data == userResponse)
        }.verifyComplete()
    }

    @Test
    fun `check login exception`() {
        every { mockUserService.login(loginUserRequest = loginUserRequest) }.returns(
            DbResponseWrapper.ServerException(exception = Exception("Exception"))
        )

        val response = userController.login(loginUserRequest)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(responseEntity.body?.errorMessage == "Exception")
        }.verifyComplete()
    }

    @Test
    fun `check logout success`() {
        val multiMap = LinkedMultiValueMap<String, String>().apply {
            this.add(HttpHeaders.AUTHORIZATION, "Bearer jhbajfhbasfjhbsafjhb")
        }

        val response = userController.logout(header = HttpHeaders(multiMap))

        assert(response.statusCode == HttpStatus.OK)
    }

    @Test
    fun `check logout without bearer token for failure`() {
        val multiMap = LinkedMultiValueMap<String, String>().apply {
            this.add(HttpHeaders.AUTHORIZATION, "")
        }

        val response = userController.logout(header = HttpHeaders(multiMap))

        assert(response.statusCode == HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `check create user success`() {
        every { mockUserService.registerUser(userRequest = userRequest) }.returns(
            DbResponseWrapper.Success(data = 0)
        )

        val response = userController.register(userRequest)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.CREATED)
            assert(responseEntity.body?.data == 0)
        }.verifyComplete()
    }

    @Test
    fun `check create user exception`() {
        every { mockUserService.registerUser(userRequest = userRequest) }.returns(
            DbResponseWrapper.UserException(exception = Exception("Exception"))
        )

        val response = userController.register(userRequest)

        StepVerifier.create(response).consumeNextWith { responseEntity ->
            assert(responseEntity.statusCode == HttpStatus.BAD_REQUEST)
            assert(responseEntity.body?.errorMessage == "Exception")
        }.verifyComplete()
    }
}