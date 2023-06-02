package com.adikmt.taskBoard.dtos.common

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap

class DbResponseWrapper<T>(
    val data: T? = null,
    val exception: Exception? = null
)

fun <T> DbResponseWrapper<T>.unwrap(
    responseStatus: ResponseStatus = ResponseStatus.OK,
    header: HttpHeaders = HttpHeaders().also {
        it.add(HttpHeaders.CACHE_CONTROL, "no-store")
        it.add(HttpHeaders.CONTENT_TYPE, "application/json")
    }
): ResponseEntity<ResponseWrapper<T>> {
    if (data != null) {
        return when (responseStatus) {
            ResponseStatus.OK -> ResponseEntity(ResponseWrapper(data = data), header, HttpStatus.OK)

            ResponseStatus.CREATED -> ResponseEntity(ResponseWrapper(data = data), header, HttpStatus.CREATED)

            ResponseStatus.ACCEPTED -> ResponseEntity(ResponseWrapper(data = data), header, HttpStatus.ACCEPTED)
        }
    } else if (exception != null) {
        return ResponseEntity(ResponseWrapper(errorMessage = exception.message), HttpStatus.INTERNAL_SERVER_ERROR)
    } else {
        return ResponseEntity(ResponseWrapper(errorMessage = "Something went wrong"), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}