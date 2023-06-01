package com.adikmt.taskBoard.services.users

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LoginUserRequest
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.UserResponse
import com.adikmt.taskBoard.repositories.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImpl
@Autowired constructor(private val userRepository: UserRepository) : UserService {
    override fun login(loginUserRequest: LoginUserRequest): DbResponseWrapper<UserResponse> {
        try {
            val user = userRepository.getUserByUserName(loginUserRequest.userID)
            user.data?.let { userResponse ->
                if (userResponse.userName == loginUserRequest.userName && userResponse.userPassword == loginUserRequest.password) {
                    return DbResponseWrapper(data = userResponse)
                } else {
                    return DbResponseWrapper(exception = Exception("Incorrect username or password"))
                }
            } ?: return DbResponseWrapper(exception = user.exception)
        } catch (e: Exception) {
            return DbResponseWrapper(exception = e)
        }
    }

    override fun registerUser(userRequest: UserRequest): DbResponseWrapper<Int?> {
        return try {
            userRepository.createUser(userRequest)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }
}