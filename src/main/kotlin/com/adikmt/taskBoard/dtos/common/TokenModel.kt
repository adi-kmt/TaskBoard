package com.adikmt.taskBoard.dtos.common

import java.util.*

data class TokenModel(
    val userId: Int,
    val token: String,
    val issuedAt: Date,
    val expiresAt: Date,
)