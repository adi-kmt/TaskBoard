package com.adikmt.taskBoard.utils

import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import java.util.concurrent.CopyOnWriteArrayList
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@Component
object SSEmitterBus {

    private val emitterList: MutableList<SseEmitter> = CopyOnWriteArrayList()

    fun addEmitter(emitter: SseEmitter) {
        emitterList.add(emitter)
    }

    fun removeEmitter(emitter: SseEmitter) {
        emitterList.remove(emitter)
    }

    @Async
    fun emit(
        cardUpdateBucketRequest: CardUpdateBucketRequest? = null,
        cardUpdateUserRequest: CardUpdateUserRequest? = null
    ) {
        val deadEmitters: MutableList<SseEmitter> = mutableListOf()
        emitterList.forEach { emitter ->
            try {
                emitter.send(
                    SseEmitter.event()
                        .comment("Update card bucket")
                        .data(cardUpdateBucketRequest ?: "No card bucket updated")
                        .comment("Update card assignee")
                        .data(cardUpdateUserRequest ?: "No card assignee updated")
                )
            } catch (e: Exception) {
                deadEmitters.add(emitter)
            }
        }
        emitterList.removeAll(deadEmitters)
    }
}