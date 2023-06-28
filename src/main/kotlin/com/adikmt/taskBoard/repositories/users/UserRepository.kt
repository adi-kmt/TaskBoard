package com.adikmt.taskBoard.repositories.users

import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.JWTUserResponse
import com.adikmt.taskBoard.dtos.responses.UserResponse


interface UserRepository {
    fun createUser(userRequest: UserRequest): DbResponseWrapper<JWTUserResponse>
    fun getUserByUserId(userId: Int): DbResponseWrapper<UserResponse>
    fun addUserToBoard(userId: Int, boardId: Int): DbResponseWrapper<Boolean>
}
