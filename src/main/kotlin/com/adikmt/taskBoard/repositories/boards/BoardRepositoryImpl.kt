package com.adikmt.taskBoard.repositories.boards

import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.common.mappers.toBoardResponse
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BoardRequest
import com.adikmt.taskBoard.dtos.responses.BoardResponse
import jooq.generated.enums.BoardsUserAddedUserRole
import jooq.generated.tables.records.BoardsRecord
import jooq.generated.tables.references.BOARDS
import jooq.generated.tables.references.BOARDS_USER_ADDED
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.concurrent.CompletableFuture

@Repository
class BoardRepositoryImpl @Autowired constructor(private val context: DSLContext) : BoardRepository {

    override fun createBoard(boardRequest: BoardRequest, userId: Int): DbResponseWrapper<Int> {
        /** In one transaction :-
         * 1. Add board to boards table
         * 2. Add user to boards-user table with role
         */
        try {
            val data: CompletableFuture<out DbResponseWrapper<Int>> = context.transactionResultAsync { configuration ->
                val boardId: Int? = DSL.using(configuration)
                    .insertInto(BOARDS)
                    .set(BOARDS.BOARD_TITLE, boardRequest.title)
                    .onDuplicateKeyIgnore()
                    .returning()
                    .fetchSingle().id

                DSL.using(configuration)
                    .insertInto(BOARDS_USER_ADDED)
                    .set(BOARDS_USER_ADDED.BOARD_ID, boardId)
                    .set(BOARDS_USER_ADDED.USER_ID, userId)
                    .set(BOARDS_USER_ADDED.USER_ROLE, BoardsUserAddedUserRole.admin)
                    .onDuplicateKeyIgnore()
                    .execute()

                return@transactionResultAsync boardId?.let {
                    DbResponseWrapper.Success(data = boardId)
                } ?: run {
                    DbResponseWrapper.ServerException(exception = Exception("Data not stored in the table"))
                }
            }.toCompletableFuture()
            return data.join()
        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun getBoardById(boardId: Int, userId: Int): DbResponseWrapper<BoardResponse> {
        /** In one transaction :-
         * 1. Join b/w boards and boards-users
         * 2. where clause to get particular id
         * 3. if none, return null
         */
        try {
            val board = context
                .select(BOARDS.ID, BOARDS.BOARD_TITLE, BOARDS.IS_BOARD_STARRED, BOARDS_USER_ADDED.USER_ID)
                .from(BOARDS)
                .leftOuterJoin(BOARDS_USER_ADDED)
                .on(BOARDS.ID.eq(BOARDS_USER_ADDED.BOARD_ID))
                .where(BOARDS_USER_ADDED.USER_ID.eq(userId).and(BOARDS.ID.eq(boardId)))
                .fetchSingle()
                .into(BoardsRecord::class.java)
                .toBoardResponse()

            return DbResponseWrapper.Success(data = board)
        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun searchBoardByName(boardName: String, userId: Int): DbResponseWrapper<List<BoardResponse>> {
        /** In one transaction :-
         * 1. Join b/w boards and boards-users
         * 2. where clause to get like board name
         * 3. if none, return null
         */
        try {
            val boardList = context
                .select(BOARDS.ID, BOARDS.BOARD_TITLE, BOARDS.IS_BOARD_STARRED, BOARDS_USER_ADDED.USER_ID)
                .from(BOARDS)
                .leftOuterJoin(BOARDS_USER_ADDED)
                .on(BOARDS.ID.eq(BOARDS_USER_ADDED.BOARD_ID))
                .where(BOARDS_USER_ADDED.USER_ID.eq(userId).and(BOARDS.BOARD_TITLE.likeIgnoreCase(boardName)))
                .fetchStreamInto(BoardsRecord::class.java)
                .toList()
                .map {
                    it.toBoardResponse()
                }

            return DbResponseWrapper.Success(data = boardList)
        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun getAllBoardsForUser(userId: Int): DbResponseWrapper<List<BoardResponse>> {
        /** In one transaction :-
         * 1. Join b/w boards and boards-users
         * 2. where clause to get for particular user
         * 3. if none, return null
         */
        try {
            val boardList = context
                .select(BOARDS.ID, BOARDS.BOARD_TITLE, BOARDS.IS_BOARD_STARRED, BOARDS_USER_ADDED.USER_ID)
                .from(BOARDS)
                .leftOuterJoin(BOARDS_USER_ADDED)
                .on(BOARDS.ID.eq(BOARDS_USER_ADDED.BOARD_ID))
                .where(BOARDS_USER_ADDED.USER_ID.eq(userId))
                .fetchStreamInto(BoardsRecord::class.java)
                .toList()
                .map {
                    it.toBoardResponse()
                }

            return DbResponseWrapper.Success(data = boardList)
        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun getUserRoleForBoard(userId: Int, boardId: Int): DbResponseWrapper<UserRole> {
        try {
            val role = context
                .select(BOARDS_USER_ADDED.USER_ROLE)
                .from(BOARDS_USER_ADDED)
                .where(BOARDS_USER_ADDED.USER_ID.eq(userId).and(BOARDS_USER_ADDED.BOARD_ID.eq(boardId)))
                .fetchSingle().component1()

            return role?.let { userRole ->
                DbResponseWrapper.Success(UserRole.valueOf(userRole.name))
            } ?: DbResponseWrapper.UserException(Exception("User and board IDs don't match"))

        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }
}
