package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.ResponseStatus
import com.adikmt.taskBoard.dtos.common.ResponseWrapper
import com.adikmt.taskBoard.dtos.common.unwrap
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import com.adikmt.taskBoard.dtos.responses.BucketResponse
import com.adikmt.taskBoard.services.buckets.BucketService
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
@RequestMapping("/api/buckets")
class BucketController @Autowired constructor(private val bucketService: BucketService) {

    @PostMapping
    fun createBucket(
        @Valid @RequestBody bucketRequest: BucketRequest,
        @RequestParam boardId: Int,
        @RequestParam userId: Int
    ): Mono<ResponseEntity<ResponseWrapper<Int?>>> {
        return try {
            Mono.just(
                bucketService.createBucket(
                    bucketRequest = bucketRequest,
                    userId = userId,
                    boardId = boardId
                ).unwrap(ResponseStatus.CREATED)
            )
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }

    @GetMapping
    fun getAllBuckets(
        @RequestParam boardId: Int,
        @RequestParam userId: Int
    ): Mono<ResponseEntity<ResponseWrapper<List<BucketResponse>?>>> {
        return try {
            Mono.just(
                bucketService.getAllBucketsForBoardId(
                    boardId = boardId
                ).unwrap())
        } catch (e: Exception) {
            Mono.just(
                ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
            )
        }
    }
}