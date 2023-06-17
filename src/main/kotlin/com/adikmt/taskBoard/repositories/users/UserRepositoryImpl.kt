package com.adikmt.taskBoard.repositories.users

import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.common.mappers.toUserResponse
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.UserRequest
import com.adikmt.taskBoard.dtos.responses.UserResponse
import jooq.generated.enums.BoardsUserAddedUserRole
import jooq.generated.tables.BoardsUserAdded.Companion.BOARDS_USER_ADDED
import jooq.generated.tables.Users
import jooq.generated.tables.Users.Companion.USERS
import jooq.generated.tables.records.BoardsUserAddedRecord
import jooq.generated.tables.records.UsersRecord
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl @Autowired constructor(private val context: DSLContext) : UserRepository {

    override fun createUser(userRequest: UserRequest): DbResponseWrapper<Int?> {
        return try {
            val userId = context.insertInto<UsersRecord>(USERS)
                .set(USERS.USER_NAME, userRequest.userName)
                .set(USERS.USER_PASSWORD, userRequest.password)
                .onDuplicateKeyIgnore()
                .returning()
                .fetchOne()?.id


            userId?.let {
                DbResponseWrapper.Success(
                    data = userId
                )
            } ?: run {
                DbResponseWrapper.ServerException(Exception("Could not store data"))
            }
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = Exception(e.message)
            )
        }
    }

    override fun getUserByUserId(userId: Int): DbResponseWrapper<UserResponse> {
        return try {
            val user = context
                .selectFrom<UsersRecord>(Users.Companion.USERS)
                .where(Users.Companion.USERS.ID.eq(userId))
                .fetchSingle()
                .toUserResponse()

            DbResponseWrapper.Success(
                data = user
            )
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = Exception(e.message)
            )
        }
    }

    override fun addUserToBoard(userId: Int, boardId: Int, role: UserRole): DbResponseWrapper<Boolean> {
        try {
            val id: Int? = context?.insertInto<BoardsUserAddedRecord>(BOARDS_USER_ADDED)
                ?.set(BOARDS_USER_ADDED.BOARD_ID, boardId)
                ?.set(BOARDS_USER_ADDED.USER_ID, userId)
                ?.set(BOARDS_USER_ADDED.USER_ROLE, BoardsUserAddedUserRole.valueOf(role.name))
                ?.onDuplicateKeyIgnore()
                ?.returningResult<Int>(BOARDS_USER_ADDED.BOARD_ID)
                ?.execute()
            return id?.let {
                DbResponseWrapper.Success(
                    data = true
                )
            } ?: DbResponseWrapper.ServerException(
                exception = Exception("Id couldn't be added to board")
            )

        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(
                exception = Exception(e.message)
            )
        }
    }
}
