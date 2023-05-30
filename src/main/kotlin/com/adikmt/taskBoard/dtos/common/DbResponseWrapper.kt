package com.adikmt.taskBoard.dtos.common

class DbResponseWrapper<T>(
    val data: T? = null,
    val exception: Exception? = null
)