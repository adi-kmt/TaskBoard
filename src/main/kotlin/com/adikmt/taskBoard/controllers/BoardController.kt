package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.ResponseStatus
import com.adikmt.taskBoard.dtos.common.wrappers.ResponseWrapper
import com.adikmt.taskBoard.dtos.common.wrappers.unwrap
import com.adikmt.taskBoard.dtos.requests.BoardRequest
import com.adikmt.taskBoard.dtos.responses.BoardResponse
import com.adikmt.taskBoard.services.boards.BoardService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/board")
class BoardController @Autowired constructor(private val boardService: BoardService) {
    /**
     * APIs (Only as applicable to particular user)
     * 1. Create Board
     * 2. Get Board by ID
     * 3. Search by board by name
     * 4. Get All Boards
     * 5. Get user role for particular board
     */
    @PostMapping
    suspend fun createBoard(
        @Valid @RequestBody boardRequest: BoardRequest,
        @AuthenticationPrincipal principal: Principal
    ): ResponseEntity<ResponseWrapper<Int>> {
        return try {
            val userId = principal.name.toInt()
            boardService.createBoard(
                boardRequest = boardRequest,
                userId = userId
            ).unwrap(successResponseStatus = ResponseStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)

        }
    }

    @GetMapping
    suspend fun getAllBoards(
        @AuthenticationPrincipal principal: Principal
    ): ResponseEntity<ResponseWrapper<List<BoardResponse>>> {
        return try {
            val userId = principal.name.toInt()
            boardService.getAllBoardsForUser(
                userId = userId
            ).unwrap()

        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    suspend fun getBoardById(
        @PathVariable id: Int,
        @AuthenticationPrincipal principal: Principal
    ): ResponseEntity<ResponseWrapper<BoardResponse>> {
        return try {
            val userId = principal.name.toInt()
            boardService.getBoardById(
                boardId = id,
                userId = userId
            ).unwrap()

        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)

        }
    }

    @GetMapping("/search/{boardName}")
    suspend fun searchBoardByName(
        @PathVariable boardName: String,
        @AuthenticationPrincipal principal: Principal
    ): ResponseEntity<ResponseWrapper<List<BoardResponse>>> {
        return try {
            val userId = principal.name.toInt()
            boardService.searchBoardByName(
                boardName = boardName,
                userId = userId
            ).unwrap()
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}