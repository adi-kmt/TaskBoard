package com.adikmt.taskBoard.services.cards

import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.CopyOnWriteArrayList

@Component
class SSEmitterBus {
    private val emitterMap: HashMap<Int, MutableList<SseEmitter>> = hashMapOf()

    fun addEmitter(boardId: Int, emitter: SseEmitter) {
        val boardEmitterList = emitterMap[boardId]
        boardEmitterList?.let { emitterList ->
            emitterList.add(emitter)
        } ?: run {
            val list = CopyOnWriteArrayList<SseEmitter>()
            val check = list.addIfAbsent(emitter)
            if (check) emitterMap[boardId] = list
        }
    }

    fun removeEmitter(boardId: Int, emitter: SseEmitter) {
        val boardEmitterList = emitterMap[boardId]
        boardEmitterList?.let { emitterList ->
            emitterList.remove(emitter)
        }
    }

    @Async
    fun emit(
        cardUpdateBucketRequest: CardUpdateBucketRequest? = null,
        cardUpdateUserRequest: CardUpdateUserRequest? = null
    ) {
        val boardId = cardUpdateBucketRequest?.boardId ?: cardUpdateUserRequest?.boardId ?: 0
        emitterMap[boardId]?.forEach { emitter ->
            if (cardUpdateBucketRequest != null) {
                emitUpdateBucket(emitter, cardUpdateBucketRequest)
            } else if (cardUpdateUserRequest != null) {
                emitUpdateUser(emitter, cardUpdateUserRequest)
            }
        }
    }


    @Async
    fun emitUpdateBucket(emitter: SseEmitter, cardUpdateBucketRequest: CardUpdateBucketRequest) {
        try {
            emitter.send(
                SseEmitter.event()
                    .comment("Update card bucket")
                    .data(cardUpdateBucketRequest)
            )
        } catch (e: Exception) {
            println(e.message)
        }
    }

    @Async
    fun emitUpdateUser(emitter: SseEmitter, cardUpdateUserRequest: CardUpdateUserRequest) {
        try {
            emitter.send(
                SseEmitter.event()
                    .comment("Update assignee user")
                    .data(cardUpdateUserRequest)
            )
        } catch (e: Exception) {
            println(e.message)
        }
    }
}