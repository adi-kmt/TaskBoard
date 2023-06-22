package com.adikmt.taskBoard.services.cards

import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.CopyOnWriteArrayList

@Component
class SSEmitterBus {
    private val emitterList: MutableList<Pair<Int, SseEmitter>> = CopyOnWriteArrayList()

    fun addEmitter(boardId: Int, emitter: SseEmitter) {
        emitterList.add(Pair(boardId, emitter))
    }

    fun removeEmitter(boardId: Int, emitter: SseEmitter) {
        emitterList.remove(Pair(boardId, emitter))
    }

    @Async
    fun emit(
        cardUpdateBucketRequest: CardUpdateBucketRequest? = null,
        cardUpdateUserRequest: CardUpdateUserRequest? = null
    ) {
        val deadEmitters: MutableList<Pair<Int, SseEmitter>> = mutableListOf()
        val boardId = cardUpdateBucketRequest?.boardId ?: cardUpdateUserRequest?.boardId ?: 0
        emitterList.forEach { emitterPair ->
            if (boardId == emitterPair.first && boardId != 0) {
                try {
                    emitterPair.second.send(
                        SseEmitter.event()
                            .comment("Update card bucket")
                            .data(cardUpdateBucketRequest ?: "No card bucket updated")
                            .comment("Update card assignee")
                            .data(cardUpdateUserRequest ?: "No card assignee updated")
                    )
                } catch (e: Exception) {
                    deadEmitters.add(emitterPair)
                }
            }
        }
        emitterList.removeAll(deadEmitters)
    }
}