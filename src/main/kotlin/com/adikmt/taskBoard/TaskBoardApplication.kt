package com.adikmt.taskBoard

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
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

@Bean
fun streamOpenApi( appVersion: String? = "1.6.0"): GroupedOpenApi? {
	val paths = arrayOf("/stream/**")
	return GroupedOpenApi.builder().group("x-stream")
		.addOpenApiCustomizer { openApi: OpenAPI ->
			openApi.info(
				Info().title("Stream API").version(appVersion)
			)
		}
		.pathsToMatch(*paths)
		.build()
}
