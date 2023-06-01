package com.adikmt.taskBoard.services.users

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LoginUserRequest
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.UserResponse

interface UserService {
    fun login(loginUserRequest: LoginUserRequest):DbResponseWrapper<UserResponse>

    fun registerUser(userRequest: UserRequest): DbResponseWrapper<Int?>
}