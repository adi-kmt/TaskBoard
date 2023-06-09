package com.adikmt.taskBoard.repositories.users

import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.UserResponse


interface UserRepository {
    /**
     * APIs to implement
     * 1. Create user
     * 2. Get User by userId
     * 3. Add user to board
     */
    fun createUser(userRequest: UserRequest): DbResponseWrapper<Int>
    fun getUserByUserName(userId: Int): DbResponseWrapper<UserResponse>
    fun addUserToBoard(userId: Int, boardId: Int, role: UserRole): DbResponseWrapper<Boolean>
}
