package com.adikmt.taskBoard.integrationtest.repositories

import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.integrationtest.MockObjects
import com.adikmt.taskBoard.integrationtest.setupDSL
import com.adikmt.taskBoard.repositories.boards.BoardRepository
import com.adikmt.taskBoard.repositories.boards.BoardRepositoryImpl
import com.adikmt.taskBoard.repositories.users.UserRepository
import com.adikmt.taskBoard.repositories.users.UserRepositoryImpl
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.test.context.ActiveProfiles

@JooqTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BoardRepositoryTest {
    lateinit var context: DSLContext

    lateinit var userRepository: UserRepository

    lateinit var boardRepository: BoardRepository

    @BeforeAll
    fun setup() {
        context = setupDSL()
        userRepository = UserRepositoryImpl(context)
        boardRepository = BoardRepositoryImpl(context)
    }

    @Test
    fun `add board`() {
        val boardResponse1 = boardRepository.createBoard(MockObjects.Board.boardRequest1, userId = 1)
        val boardResponse2 = boardRepository.createBoard(MockObjects.Board.boardRequest2, userId = 2)

        assert(boardResponse1 == DbResponseWrapper.Success(1))
        assert(boardResponse2 == DbResponseWrapper.Success(2))
    }

    @Test
    fun `add user to board`() {
        val userResponse1 = userRepository.addUserToBoard(userId = 2, boardId = 1)

        assert(userResponse1 == DbResponseWrapper.Success(true))
    }

    @Test
    fun `add same user to board`() {
        //With on Duplicate Key ignore, table doesn't add column but success response with true appears
        val userResponse = userRepository.addUserToBoard(userId = 2, boardId = 1)

        assert(userResponse == DbResponseWrapper.Success(true))
    }

    @Test
    fun `get board by id`() {
        val boardResponse = boardRepository.getBoardById(boardId = 1, userId = 2)

        assert(boardResponse == DbResponseWrapper.Success(MockObjects.Board.boardResponseList.first()))
    }

    @Test
    fun `get invalid board id`() {
        assertThrows<Exception>(message = "cursor returns no rows") {
            val boardResponse = boardRepository.getBoardById(boardId = 2, userId = 1)
            throw (boardResponse as DbResponseWrapper.ServerException).exception
        }
    }

    @Test
    fun `search board by board name`() {
        val boardResponse = boardRepository.searchBoardByName(
            boardName = MockObjects.Board.boardRequest1.title,
            userId = 1
        )
        assert(boardResponse == DbResponseWrapper.Success(listOf(MockObjects.Board.boardResponseList.first())))
    }

    @Test
    fun `search invalid board`() {
        val boardResponse = boardRepository.searchBoardByName(
            boardName = MockObjects.Board.boardRequest2.title,
            userId = 1
        )
        val data = (boardResponse as DbResponseWrapper.Success).data
        assert(data.isEmpty())
    }

    @Test
    fun `get all boards for user`() {
        val boardResponse = boardRepository.getAllBoardsForUser(userId = 2)

        val data = (boardResponse as DbResponseWrapper.Success).data

        assert(data.size == 2)
        assert(data == MockObjects.Board.boardResponseList)
    }

    @Test
    fun `get user role for board`() {
        val roleForBoard1 = boardRepository.getUserRoleForBoard(userId = 2, boardId = 1)
        val roleForBoard2 = boardRepository.getUserRoleForBoard(userId = 2, boardId = 2)


        assert(roleForBoard1 == DbResponseWrapper.Success(UserRole.user))
        assert(roleForBoard2 == DbResponseWrapper.Success(UserRole.admin))
    }

    @Test
    fun `check invalid user role`() {
        assertThrows<Exception>(message = "cursor returns no rows") {
            val roleForBoard2 = boardRepository.getUserRoleForBoard(userId = 1, boardId = 2)
            throw (roleForBoard2 as DbResponseWrapper.ServerException).exception
        }

    }
}