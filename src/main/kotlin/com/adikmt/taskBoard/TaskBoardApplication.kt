package com.adikmt.taskBoard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class TaskBoardApplication

fun main(args: Array<String>) {
	runApplication<TaskBoardApplication>(*args)
}
