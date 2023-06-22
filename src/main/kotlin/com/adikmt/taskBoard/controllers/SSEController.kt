package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.services.cards.SSEmitterBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/api/cards")
class SSEController @Autowired constructor(private val sseEmitterBus: SSEmitterBus) {

    @GetMapping("/updates/{boardId}")
    fun getCardUpdates(
        @PathVariable boardId: Int
    ): ResponseEntity<SseEmitter> {
        val sseEmitter = SseEmitter()
        sseEmitterBus.addEmitter(boardId, sseEmitter)

        sseEmitter.onCompletion {
            sseEmitterBus.removeEmitter(boardId, sseEmitter)
        }
        sseEmitter.onTimeout {
            sseEmitterBus.removeEmitter(boardId, sseEmitter)
        }
        return ResponseEntity.ok(sseEmitter)
    }
}