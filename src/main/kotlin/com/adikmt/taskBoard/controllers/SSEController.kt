package com.adikmt.taskBoard.controllers

import com.adikmt.taskBoard.utils.SSEmitterBus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping("/cards/sseUpdate")
class SSEController {

    private val emitterBus = SSEmitterBus

    @GetMapping
    fun update(): ResponseEntity<SseEmitter> {
        val emitter = SseEmitter()
        emitterBus.addEmitter(emitter)

        emitter.onCompletion {
            emitterBus.removeEmitter(emitter)
        }
        emitter.onTimeout {
            emitterBus.removeEmitter(emitter)
        }
        return ResponseEntity.ok(emitter)
    }
}