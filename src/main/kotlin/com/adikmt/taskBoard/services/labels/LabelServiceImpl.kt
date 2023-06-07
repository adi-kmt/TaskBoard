package com.adikmt.taskBoard.services.labels

import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.LabelRequest
import com.adikmt.taskBoard.dtos.responses.LabelResponse
import com.adikmt.taskBoard.repositories.boards.BoardRepository
import com.adikmt.taskBoard.repositories.labels.LabelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LabelServiceImpl
@Autowired constructor(
    private val labelRepository: LabelRepository,
    private val boardRepository: BoardRepository
) : LabelService {

    override fun createLabel(labelRequest: LabelRequest, userId: Int, boardId: Int): DbResponseWrapper<out Int?> {
        try {
            val userRole = boardRepository.getUserRoleForBoard(userId = userId, boardId = boardId)

            return when (userRole) {
                is DbResponseWrapper.Success -> {
                    if (userRole.data?.equals(UserRole.ADMIN) == true) {
                        labelRepository.createLabel(labelRequest)
                    } else {
                        DbResponseWrapper.UserException(exception = Exception("Only admins can create labels"))
                    }
                }

                else -> DbResponseWrapper.UserException(exception = Exception("Something went wrong"))
            }
        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(exception = e)
        }
    }

    override fun getAllLabels(): DbResponseWrapper<out List<LabelResponse>?> {
        return try {
            labelRepository.getAllLabels()
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(exception = e)
        }
    }
}