package com.adikmt.taskBoard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskBoardApplication

fun main(args: Array<String>) {
	runApplication<TaskBoardApplication>(*args)
}
