package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.ResponseStatus
import com.adikmt.taskBoard.dtos.common.wrappers.ResponseWrapper
import com.adikmt.taskBoard.dtos.common.wrappers.unwrap
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse
import com.adikmt.taskBoard.services.labels.LabelService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/labels")
class LabelController @Autowired constructor(private val labelService: LabelService) {

    @GetMapping
    fun getAllLabels(): Mono<ResponseEntity<ResponseWrapper<List<LabelResponse>?>>> {
        return try {
            Mono.just(
                labelService.getAllLabels().unwrap()
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }

    @PostMapping
    fun addLabel(
        @Valid @RequestBody labelRequest: LabelRequest,
        @RequestParam userId: Int,
        @RequestParam boardId: Int
    ): Mono<ResponseEntity<ResponseWrapper<Int?>>> {
        return try {
            Mono.just(
                labelService.createLabel(
                    labelRequest = labelRequest,
                    userId = userId,
                    boardId = boardId
                ).unwrap(successResponseStatus = ResponseStatus.CREATED)
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }
}