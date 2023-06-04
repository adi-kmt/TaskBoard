package com.adikmt.taskBoard.services.buckets

import com.adikmt.taskBoard.dtos.common.UserRole
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.BucketRequest
import com.adikmt.taskBoard.dtos.responses.BucketResponse
import com.adikmt.taskBoard.repositories.boards.BoardRepository
import com.adikmt.taskBoard.repositories.buckets.BucketRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BucketServiceImpl
@Autowired constructor(
    private val bucketRepository: BucketRepository,
    private val boardRepository: BoardRepository
) : BucketService {
    @Transactional
    override fun createBucket(bucketRequest: BucketRequest, userId: Int, boardId: Int): DbResponseWrapper<out Int?> {
        try {
            val userRole = boardRepository.getUserRoleForBoard(userId = userId, boardId = boardId)
            return when (userRole) {
                is DbResponseWrapper.Success -> {
                    if (userRole.data?.equals(UserRole.ADMIN) == true) {
                        bucketRepository.createBucket(bucketRequest)
                    } else {
                        DbResponseWrapper.UserException(exception = Exception("Non admins can't create Buckets"))
                    }
                }

                else -> DbResponseWrapper.UserException(exception = Exception("Something went wrong"))
            }
        } catch (e: Exception) {
            return DbResponseWrapper.ServerException(exception = e)
        }
    }

    override fun getAllBucketsForBoardId(boardId: Int): DbResponseWrapper<out List<BucketResponse>?> {
        return try {
            bucketRepository.getAllBucketsForBoardId(boardId)
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(exception = e)
        }
    }
}