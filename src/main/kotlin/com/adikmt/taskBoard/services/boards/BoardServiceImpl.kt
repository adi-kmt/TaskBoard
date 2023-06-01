package com.adikmt.taskBoard.services.boards

import com.adikmt.taskBoard.dtos.common.DbResponseWrapper
import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.requests.AddUserToBoardRequest
import com.adikmt.taskBoard.dtos.requests.BoardRequest
import com.adikmt.taskBoard.dtos.responses.BoardResponse
import com.adikmt.taskBoard.repositories.boards.BoardRepository
import com.adikmt.taskBoard.repositories.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired

class BoardServiceImpl
@Autowired constructor(
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository
) : BoardService {
    override fun createBoard(boardRequest: BoardRequest, userId: Int): DbResponseWrapper<Int> {
        return try {
            boardRepository.createBoard(boardRequest, userId)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }

    override fun addUserToBoard(userId: Int, addUserToBoardRequest: AddUserToBoardRequest): DbResponseWrapper<Boolean> {
        return try {
            val userRole = boardRepository.getUserRoleForBoard(userId, addUserToBoardRequest.boardId)
            userRole.data?.let { role ->
                if (role == UserRole.ADMIN) {
                    userRepository.addUserToBoard(addUserToBoardRequest)
                } else {
                    DbResponseWrapper(exception = Exception("Non admins can't add user to board'"))
                }
            } ?: DbResponseWrapper(exception = userRole.exception)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }

    override fun getBoardById(boardId: Int, userId: Int): DbResponseWrapper<BoardResponse?> {
        return try {
            boardRepository.getBoardById(boardId = boardId, userId = userId)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }

    override fun searchBoardByName(boardName: String, userId: Int): DbResponseWrapper<List<BoardResponse>?> {
        return try {
            boardRepository.searchBoardByName(boardName = boardName, userId = userId)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }

    override fun getAllBoardsForUser(userId: Int): DbResponseWrapper<List<BoardResponse>?> {
        return try {
            boardRepository.getAllBoardsForUser(userId = userId)
        } catch (e: Exception) {
            DbResponseWrapper(exception = e)
        }
    }
}