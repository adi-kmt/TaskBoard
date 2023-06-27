package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BoardRequest
import com.adikmt.taskBoard.dtos.responses.BoardResponse
import com.adikmt.taskBoard.services.boards.BoardService
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.test.context.support.WithMockUser

@SpringBootTest
class BoardControllerTest {

    private val boardService = mockk<BoardService>()

    private val boardController = BoardController(boardService)

    private val boardRequest = BoardRequest(title = "Board title")

    private val boardResponse = BoardResponse(boardId = 1, title = "Board title")

    @Test
    @WithMockUser(username = "1")
    fun `create board successfully`() {
        runBlocking {
            every { boardService.createBoard(boardRequest = boardRequest, userId = 1) } returns (
                    DbResponseWrapper.Success(data = 1)
                    )

            val mockPrincipal: Authentication = UsernamePasswordAuthenticationToken("1", null)

            val response = boardController.createBoard(boardRequest = boardRequest, mockPrincipal)

            assert(response.statusCode == HttpStatus.CREATED)
            assert(response.body?.data == 1)
        }
    }

    @Test
    @WithMockUser(username = "1")
    fun `create board with exception`() {
        runBlocking {
            every { boardService.createBoard(boardRequest = boardRequest, userId = 1) } returns (
                    DbResponseWrapper.DBException(exception = Exception("Exception"))
                    )
            val mockPrincipal: Authentication = UsernamePasswordAuthenticationToken("1", null)

            val response = boardController.createBoard(boardRequest = boardRequest, mockPrincipal)

            assert(response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(response.body?.errorMessage == "Exception")
        }
    }

    @Test
    @WithMockUser(username = "1")
    fun `Get board successfully`() {
        runBlocking {
            every { boardService.getBoardById(boardId = 1, userId = 1) } returns (
                    DbResponseWrapper.Success(data = boardResponse)
                    )

            val mockPrincipal: Authentication = UsernamePasswordAuthenticationToken("1", null)
            val response = boardController.getBoardById(id = 1, mockPrincipal)

            assert(response.statusCode == HttpStatus.OK)
            assert(response.body?.data == boardResponse)
        }
    }

    @Test
    @WithMockUser(username = "1")
    fun `Get board with exception`() {
        runBlocking {
            every { boardService.getBoardById(boardId = 1, userId = 1) } returns (
                    DbResponseWrapper.ServerException(exception = Exception("Exception"))
                    )
            val mockPrincipal: Authentication = UsernamePasswordAuthenticationToken("1", null)
            val response = boardController.getBoardById(id = 1, mockPrincipal)

            assert(response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(response.body?.errorMessage == "Exception")
        }
    }

    @Test
    @WithMockUser(username = "1")
    fun `Get board by user successfully`() {
        runBlocking {
            every { boardService.getAllBoardsForUser(userId = 1) } returns (
                    DbResponseWrapper.Success(data = listOf(boardResponse))
                    )
            val mockPrincipal: Authentication = UsernamePasswordAuthenticationToken("1", null)
            val response = boardController.getAllBoards(mockPrincipal)

            assert(response.statusCode == HttpStatus.OK)
            assert(response.body?.data == listOf(boardResponse))
        }
    }

    @Test
    @WithMockUser(username = "1")
    fun `Get board by user with exception`() {
        runBlocking {
            every { boardService.getAllBoardsForUser(userId = 1) } returns (
                    DbResponseWrapper.DBException(exception = Exception("Exception"))
                    )
            val mockPrincipal: Authentication = UsernamePasswordAuthenticationToken("1", null)
            val response = boardController.getAllBoards(mockPrincipal)

            assert(response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(response.body?.errorMessage == "Exception")
        }
    }

    @Test
    @WithMockUser(username = "1")
    fun `search board successfully`() {
        runBlocking {
            every { boardService.searchBoardByName(userId = 1, boardName = "Board title") } returns (
                    DbResponseWrapper.Success(data = listOf(boardResponse))
                    )
            val mockPrincipal: Authentication = UsernamePasswordAuthenticationToken("1", null)
            val response = boardController.searchBoardByName(boardName = "Board title", mockPrincipal)

            assert(response.statusCode == HttpStatus.OK)
            assert(response.body?.data == listOf(boardResponse))
        }
    }

    @Test
    @WithMockUser(username = "1")
    fun `search board with exception`() {
        runBlocking {
            every { boardService.searchBoardByName(userId = 1, boardName = "Board title") } returns (
                    DbResponseWrapper.DBException(exception = Exception("Exception"))
                    )
            val mockPrincipal: Authentication = UsernamePasswordAuthenticationToken("1", null)
            val response = boardController.searchBoardByName(boardName = "Board title", mockPrincipal)

            assert(response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
            assert(response.body?.errorMessage == "Exception")
        }
    }
}