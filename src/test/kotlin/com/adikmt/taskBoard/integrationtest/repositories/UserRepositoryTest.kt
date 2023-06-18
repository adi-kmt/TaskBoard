package com.adikmt.taskBoard.integrationtest.repositories

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.integrationtest.MockObjects
import com.adikmt.taskBoard.integrationtest.setupDSL
import com.adikmt.taskBoard.repositories.users.UserRepository
import com.adikmt.taskBoard.repositories.users.UserRepositoryImpl
import org.jooq.DSLContext
import org.junit.jupiter.api.*
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.test.context.ActiveProfiles

@JooqTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {
    lateinit var context: DSLContext

    lateinit var userRepository: UserRepository

    @BeforeAll
    fun setup() {
        context = setupDSL()
        userRepository = UserRepositoryImpl(context)
    }

    @Test
    fun `add user to db`() {
        val userResponse1 = userRepository.createUser(MockObjects.User.userRequest1)
        val userResponse2 = userRepository.createUser(MockObjects.User.userRequest2)

        assert(userResponse1 == DbResponseWrapper.Success(data = 1))
        assert(userResponse2 == DbResponseWrapper.Success(data = 2))
    }

    @Test
    fun `get user from db`() {
        val userResponse = userRepository.getUserByUserId(1)
        assert(userResponse == DbResponseWrapper.Success(MockObjects.User.userResponseList.first()))
    }

    @Test
    fun `user not present in db`() {
        assertThrows<Exception> {
            val userResponse = userRepository.getUserByUserId(0)
            throw (userResponse as DbResponseWrapper.ServerException).exception
        }
    }
}