package com.adikmt.taskBoard

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@ExperimentalCoroutinesApi
@FlowPreview
@EnableAsync
class TaskBoardApplication

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
fun main(args: Array<String>) {
	runApplication<TaskBoardApplication>(*args)
}
