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
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/board")
class BoardController @Autowired constructor(private val boardService: BoardService) {

    @PostMapping
    fun createBoard(
        @Valid @RequestBody boardRequest: BoardRequest
    ): ResponseEntity<ResponseWrapper<Int>> {
        return try {
            val userId = (ReactiveSecurityContextHolder.getContext()
                .block()?.authentication?.principal as UserDetails).username.toInt()
            boardService.createBoard(
                boardRequest = boardRequest,
                userId = userId
            ).unwrap(successResponseStatus = ResponseStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)

        }
    }

    @GetMapping
    fun getAllBoards(): ResponseEntity<ResponseWrapper<List<BoardResponse>>> {
        return try {
            val userId = (ReactiveSecurityContextHolder.getContext()
                .block()?.authentication?.principal as UserDetails).username.toInt()
            boardService.getAllBoardsForUser(
                userId = userId
            ).unwrap()

        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/{id}")
    fun getBoardById(
        @PathVariable id: Int
    ): ResponseEntity<ResponseWrapper<BoardResponse>> {
        return try {
            val userId = (ReactiveSecurityContextHolder.getContext()
                .block()?.authentication?.principal as UserDetails).username.toInt()
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
        @PathVariable boardName: String
    ): ResponseEntity<ResponseWrapper<List<BoardResponse>>> {
        return try {
            val userId = (ReactiveSecurityContextHolder.getContext()
                .block()?.authentication?.principal as UserDetails).username.toInt()
            boardService.searchBoardByName(
                boardName = boardName,
                userId = userId
            ).unwrap()
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}