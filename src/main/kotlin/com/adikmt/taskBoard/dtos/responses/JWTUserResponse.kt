package com.adikmt.taskBoard.dtos.responses

data class JWTUserResponse(
    val userId: Int,
    val token: String
)