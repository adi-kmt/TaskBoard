package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LoginUserRequest
import com.adikmt.taskBoard.dtos.responses.UserResponse
import com.adikmt.taskBoard.services.users.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest {
    @MockkBean(relaxed = true)
    private lateinit var mockUserService: UserService

//    @MockkBean(relaxed = true)
//    private lateinit var mockUserRepository: UserRepository

    private lateinit var userController: UserController

    @Autowired
    private lateinit var webTestClient: WebTestClient

    private val loginUserRequest = LoginUserRequest(userID = 0, userName = "Namessa", password = "Password")

    private val userResponse = UserResponse(
        userId = 0,
        userName = "Namessa",
        userPassword = "Password",
        jwtToken = "kjnwdekjndewkjndwekjn.dewkjndewkjnwed.kjndwekjnwd",
    )

//    @BeforeEach
//    fun setup() {
////        userController = UserController(mockUserService)
//        webTestClient = WebTestClient.bindToController(userController).build()
//    }

    @Test
//    @WithMockUser
    fun `check login success`() {
//        every { mockUserRepository.getUserByUserName(loginUserRequest.userID) }.returns(
//            DbResponseWrapper.Success(userResponse)
//        )

        every { mockUserService.login(loginUserRequest = loginUserRequest) }.returns(
            DbResponseWrapper.Success(data = userResponse)
        )

        val response = webTestClient.post()
            .uri("/api/login")
            .body(Mono.just(loginUserRequest), LoginUserRequest::class.java)
            .exchange()

//        val response = userController.login(loginUserRequest)

        response.expectStatus().isOk

//        StepVerifier.create(response).consumeNextWith { responseEntity->
//            assert(responseEntity.statusCode == HttpStatus.OK)
//            assert(responseEntity.body?.data == userResponse)
    }
//    }

//    @Test
//    @WithMockUser
//    fun `check logout success`() {
//        val multiMap = LinkedMultiValueMap<String, String>().apply {
//            this.add(HttpHeaders.AUTHORIZATION, "Bearer jhbajfhbasfjhbsafjhb")
//        }
//
//        val response = userController.logout(header = HttpHeaders(multiMap))
//
//        assert(response.statusCode == HttpStatus.OK)
//    }
}