package com.adikmt.taskBoard

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync


@SpringBootApplication
@ExperimentalCoroutinesApi
@FlowPreview
@EnableAsync
@EnableRetry
@OpenAPIDefinition(
    info = io.swagger.v3.oas.annotations.info.Info(
        title = "Taskboard APIs",
        version = "1.0",
        description = "Documentation APIs v1.0"
    )
)
class TaskBoardApplication

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
fun main(args: Array<String>) {
    runApplication<TaskBoardApplication>(*args)
}

@Bean
fun streamOpenApi(appVersion: String? = "1.6.0"): GroupedOpenApi? {
    val paths = arrayOf("/stream/**")
    return GroupedOpenApi.builder()
        .group("x-stream")
        .addOpenApiCustomizer { openApi: OpenAPI ->
            openApi.info(
                Info().title("Taskboard API").version(appVersion)
            )
        }
        .pathsToMatch(*paths)
        .build()
}
