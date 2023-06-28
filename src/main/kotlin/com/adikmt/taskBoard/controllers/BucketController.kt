package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.common.wrappers.ResponseStatus
import com.adikmt.taskBoard.dtos.common.wrappers.ResponseWrapper
import com.adikmt.taskBoard.dtos.common.wrappers.unwrap
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import com.adikmt.taskBoard.dtos.responses.BucketResponse
import com.adikmt.taskBoard.services.buckets.BucketService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/buckets")
class BucketController @Autowired constructor(private val bucketService: BucketService) {

    /**
     * APIs
     * 1. Create bucket (Only can be done by admin)
     * 2. Get All buckets
     */

    @PostMapping
    suspend fun createBucket(
        @Valid @RequestBody bucketRequest: BucketRequest,
        @AuthenticationPrincipal principal: Principal
    ): ResponseEntity<ResponseWrapper<Int>> {
        return try {
            val userId = principal.name.toInt()
            bucketService.createBucket(
                bucketRequest = bucketRequest,
                userId = userId
            ).unwrap(ResponseStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping
    suspend fun getAllBuckets(
        @RequestParam boardId: Int
    ): ResponseEntity<ResponseWrapper<List<BucketResponse>>> {
        return try {
            bucketService.getAllBucketsForBoardId(
                boardId = boardId
            ).unwrap()
        } catch (e: Exception) {
            ResponseEntity(ResponseWrapper(errorMessage = e.message), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}