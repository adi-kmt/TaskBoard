package com.adikmt.taskBoard.dtos.common.wrappers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

sealed class DbResponseWrapper<T> {
    data class Success<T>(val data: T) : DbResponseWrapper<T>()

    data class DBException(val exception: Exception) : DbResponseWrapper<Nothing>()

    data class UserException(val exception: Exception) : DbResponseWrapper<Nothing>()

    data class ServerException(val exception: Exception) : DbResponseWrapper<Nothing>()
}

fun <T> DbResponseWrapper<T>.unwrap(
    successResponseStatus: ResponseStatus = ResponseStatus.OK,
    header: HttpHeaders = HttpHeaders().also {
        it.add(HttpHeaders.CACHE_CONTROL, "no-store")
        it.add(HttpHeaders.CONTENT_TYPE, "application/json")
    }
): ResponseEntity<ResponseWrapper<T>> {
    return when (this) {
        is DbResponseWrapper.Success -> {
            when (successResponseStatus) {
                ResponseStatus.OK -> ResponseEntity(ResponseWrapper(data = data), header, HttpStatus.OK)

                ResponseStatus.CREATED -> ResponseEntity(ResponseWrapper(data = data), header, HttpStatus.CREATED)

                ResponseStatus.ACCEPTED -> ResponseEntity(ResponseWrapper(data = data), header, HttpStatus.ACCEPTED)
            }
        }

        is DbResponseWrapper.DBException -> ResponseEntity(
            ResponseWrapper(errorMessage = exception.message),
            HttpStatus.INTERNAL_SERVER_ERROR
        )

        is DbResponseWrapper.UserException -> ResponseEntity(
            ResponseWrapper(errorMessage = exception.message),
            HttpStatus.BAD_REQUEST
        )

        is DbResponseWrapper.ServerException -> ResponseEntity(
            ResponseWrapper(errorMessage = exception.message),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}