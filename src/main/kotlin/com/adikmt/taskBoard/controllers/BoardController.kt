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
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/board")
class BoardController @Autowired constructor(private val boardService: BoardService) {

    @PostMapping
    fun createBoard(
        @Valid @RequestBody boardRequest: BoardRequest,
        @RequestParam userId: Int
    ): ResponseEntity<ResponseWrapper<Int>> {
        return try {
            boardService.createBoard(
                boardRequest = boardRequest,
                userId = userId
            ).unwrap(successResponseStatus = ResponseStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)

        }
    }

    @GetMapping
    fun getAllBoards(
        @RequestParam userId: Int
    ): ResponseEntity<ResponseWrapper<List<BoardResponse>>> {
        return try {
            boardService.getAllBoardsForUser(
                userId = userId
            ).unwrap()

        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun getBoardById(
        @PathVariable id: Int,
        @RequestParam userId: Int
    ): ResponseEntity<ResponseWrapper<BoardResponse>> {
        return try {
            boardService.getBoardById(
                boardId = id,
                userId = userId
            ).unwrap()

        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)

        }
    }

    @GetMapping("/search/{boardName}")
    fun searchBoardByName(
        @PathVariable boardName: String,
        @RequestParam userId: Int
    ): ResponseEntity<ResponseWrapper<List<BoardResponse>>> {
        return try {
            boardService.searchBoardByName(
                boardName = boardName,
                userId = userId
            ).unwrap()
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}