package com.adikmt.taskBoard.dtos.common


class ResponseWrapper<T>(
    val data: T? = null,
    val errorMessage: String? = null
)

enum class ResponseStatus{
    CREATED, OK, ACCEPTED
}