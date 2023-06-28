package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.services.cards.CardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/cards")
class SSEController @Autowired constructor(private val cardService: CardService) {

    @GetMapping("/updateAssignee/{boardId}")
    fun getAssigneeUpdates(
        @PathVariable boardId: Int
    ): Flux<ServerSentEvent<CardUpdateUserRequest>> {
        return cardService.updateUserSink.asFlux()
            .map {
                ServerSentEvent
                    .builder(it)
                    .event("Update assigned user")
                    .build()
            }
    }

    @GetMapping("/updateBucket/{boardId}")
    fun getCardUpdates(
        @PathVariable boardId: Int
    ): Flux<ServerSentEvent<CardUpdateBucketRequest>> {
        return cardService.updateCardSink.asFlux()
            .map {
                ServerSentEvent
                    .builder(it)
                    .event("Update Bucket in Board")
                    .build()
            }
    }
}