package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LoginUserRequest
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.JWTUserResponse
import com.adikmt.taskBoard.dtos.responses.UserResponse
import com.adikmt.taskBoard.security.JwtSupport
import com.adikmt.taskBoard.services.users.UserService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest
class UserControllerTest {

    private val mockUserService: UserService = mockk<UserService>()

    private val mockJwt = mockk<JwtSupport>()

    private val userController: UserController = UserController(mockUserService, mockJwt)

    private val loginUserRequest = LoginUserRequest(userID = 0, userName = "Namessa", password = "Password")

    private val userRequest = UserRequest(userName = "Namessa", password = "Password")

    private val userResponse = UserResponse(
        userId = 0,
        userName = "Namessa",
        userPassword = "Password",
    )

    private val userJWTToken = JWTUserResponse(userId = 0, token = "token")

    @Test
    fun `check login success`() {
        runBlocking {
            every { mockUserService.login(loginUserRequest = loginUserRequest) }.returns(
                DbResponseWrapper.Success(data = userResponse)
            )

            val response = userController.login(loginUserRequest)

            assert(response.statusCode == HttpStatus.OK)
            assert(response.body?.data == userResponse)
        }
    }

    @Test
    fun `check login exception`() {
        runBlocking {
            every { mockUserService.login(loginUserRequest = loginUserRequest) }.returns(
                DbResponseWrapper.ServerException(exception = Exception("Exception"))
            )

            val response = userController.login(loginUserRequest)

            assert(response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(response.body?.errorMessage == "Exception")
        }
    }

    @Test
    fun `check create user success`() {
        runBlocking {
            every { mockUserService.registerUser(userRequest = userRequest) }.returns(
                DbResponseWrapper.Success(data = userJWTToken)
            )

            val response = userController.register(userRequest)

            assert(response.statusCode == HttpStatus.CREATED)
            assert(response.body?.data == userJWTToken)
        }
    }

    @Test
    fun `check create user exception`() {
        runBlocking {
            every { mockUserService.registerUser(userRequest = userRequest) }.returns(
                DbResponseWrapper.UserException(exception = Exception("Exception"))
            )

            val response = userController.register(userRequest)

            assert(response.statusCode == HttpStatus.BAD_REQUEST)
            assert(response.body?.errorMessage == "Exception")
        }
    }
}