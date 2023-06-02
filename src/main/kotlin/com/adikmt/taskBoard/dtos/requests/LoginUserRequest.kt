package com.adikmt.taskBoard.dtos.requests

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

class LoginUserRequest(
    @NotNull
    val userID: Int,
    @NotNull
    @Size(min = 5, max = 30, message = "Please enter a username that lies between 5 and 30 characters")
    val userName: String,
    @NotNull
    @Size(min = 5, max = 20, message = "Please enter a password that lies between 5 and 20 characters")
    val password: String
)