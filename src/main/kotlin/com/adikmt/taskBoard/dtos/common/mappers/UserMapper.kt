package com.adikmt.taskBoard.dtos.common.mappers

import com.adikmt.taskBoard.dtos.responses.JWTUserResponse
import com.adikmt.taskBoard.dtos.responses.UserResponse
import com.adikmt.taskBoard.security.JWTUtil
import jooq.generated.tables.records.UsersRecord

fun UsersRecord.toUserResponse() = UserResponse(
    userId = this.id,
    userName = this.userName,
    userPassword = this.userPassword
)

fun jetUserResponse(userId: Int) = JWTUserResponse(
    userId = userId,
    token = JWTUtil().generateToken(userId.toString())
)