package com.adikmt.taskBoard.repositories.cards

import com.adikmt.taskBoard.dtos.common.mappers.toCardResponse
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import jooq.generated.tables.records.CardsRecord
import jooq.generated.tables.references.BOARDS
import jooq.generated.tables.references.BUCKETS
import jooq.generated.tables.references.CARDS
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.stream.Collectors

@Repository
class CardRepositoryImpl @Autowired constructor(private val context: DSLContext) : CardRepository {

    init {
        context.settings()
            .withUpdateRecordTimestamp(true)
            .withExecuteWithOptimisticLocking(true)
            .withExecuteWithOptimisticLockingExcludeUnversioned(true)
    }

    override fun createCard(cardRequest: CardRequest, userId: Int): DbResponseWrapper<Int> {
        return try {
            val cardId = context.insertInto<CardsRecord>(CARDS)
                .set(CARDS.CARD_TITLE, cardRequest.title)
                .set(CARDS.CARD_DESC, cardRequest.description)
                .set(CARDS.IS_CARD_ARCHIVED, cardRequest.isCardArchived)
                .set(CARDS.BUCKET_ID, cardRequest.bucketId)
                .set(CARDS.USER_ASSIGNED_ID, userId)
                .set(CARDS.LABEL_ID, cardRequest.labelId)
                .set(CARDS.CARD_START_DATE, LocalDateTime.parse(cardRequest.startDate))
                .set(CARDS.CARD_END_DATE, LocalDateTime.parse(cardRequest.endDate))
                .onDuplicateKeyIgnore()
                .returning()
                .fetchSingle().id

            cardId?.let {
                DbResponseWrapper.Success(
                    data = cardId
                )
            } ?: run {
                DbResponseWrapper.ServerException(Exception("Failed to save into table"))
            }
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun getAllCards(boardId: Int): List<DbResponseWrapper<CardResponse>> {
        return try {
            context.select()
                .from(BOARDS)
                .innerJoin(BUCKETS).on(BOARDS.ID.eq(BUCKETS.BOARD_ID))
                .innerJoin(CARDS).on(BUCKETS.ID.eq(CARDS.BUCKET_ID))
                .where(BOARDS.ID.eq(boardId).and(CARDS.IS_CARD_ARCHIVED.isFalse))
                .orderBy(CARDS.CARD_END_DATE)
                .forUpdate()
                .fetchStreamInto(CardsRecord::class.java)
                .collect(Collectors.toList())
                .map {
                    DbResponseWrapper.Success(it.toCardResponse())
                }
        } catch (e: Exception) {
            listOf(
                DbResponseWrapper.ServerException(
                    exception = e
                )
            )
        }
    }

    override fun getAllCardsAssignedToUserById(
        userId: Int,
        boardId: Int
    ): List<DbResponseWrapper<CardResponse>> {
        return try {
            context.select()
                .from(BOARDS)
                .innerJoin(BUCKETS).on(BOARDS.ID.eq(BUCKETS.BOARD_ID))
                .innerJoin(CARDS).on(BUCKETS.ID.eq(CARDS.BUCKET_ID))
                .where(
                    BOARDS.ID.eq(boardId)
                        .and(CARDS.IS_CARD_ARCHIVED.isFalse)
                        .and(CARDS.USER_ASSIGNED_ID.eq(userId))
                )
                .orderBy(CARDS.CARD_END_DATE)
                .forUpdate()
                .fetchStreamInto(CardsRecord::class.java)
                .collect(Collectors.toList())
                .map {
                    DbResponseWrapper.Success(it.toCardResponse())
                }
        } catch (e: Exception) {
            listOf(
                DbResponseWrapper.ServerException(
                    exception = e
                )
            )
        }
    }

    @Retryable(
        value = [org.jooq.exception.DataChangedException::class], backoff = Backoff(delay = 100),
        maxAttempts = 2
    )
    override fun updateCardDetails(cardRequest: CardUpdateRequest): DbResponseWrapper<Boolean> {
        return try {

            val updateRecord = CardsRecord().apply {
                cardDesc = cardRequest.description
                isCardArchived = cardRequest.isCardArchived
                cardEndDate = LocalDateTime.parse(cardRequest.endDate)
                labelId = cardRequest.labelId
            }

            val hasCardBeenUpdated = context
                .update(CARDS)
                .set(updateRecord)
                .where(CARDS.ID.eq(cardRequest.id))
                .execute()

            if (hasCardBeenUpdated == 1) {
                DbResponseWrapper.Success(data = true)
            } else {
                DbResponseWrapper.UserException(Exception("No card found to update"))
            }
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    @Retryable(
        value = [org.jooq.exception.DataChangedException::class], backoff = Backoff(delay = 30000),
        maxAttempts = 2
    )
    override fun updateCardBucket(cardUpdateBucketRequest: CardUpdateBucketRequest): DbResponseWrapper<Boolean> {
        return try {
            val hasCardBeenUpdated = context.update(CARDS)
                .set(CARDS.BUCKET_ID, cardUpdateBucketRequest.bucketId)
                .where(CARDS.ID.eq(cardUpdateBucketRequest.id))
                .execute()

            if (hasCardBeenUpdated == 1) {
                DbResponseWrapper.Success(data = true)
            } else {
                DbResponseWrapper.UserException(Exception("No card found to update"))
            }
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    @Retryable(
        value = [org.jooq.exception.DataChangedException::class], backoff = Backoff(delay = 100),
        maxAttempts = 2
    )
    override fun assignCardToAnotherUser(cardUpdateUserRequest: CardUpdateUserRequest): DbResponseWrapper<Boolean> {
        return try {
            val hasCardBeenUpdated = context.update(CARDS)
                .set(CARDS.USER_ASSIGNED_ID, cardUpdateUserRequest.newUserId)
                .where(CARDS.ID.eq(cardUpdateUserRequest.id))
                .execute()

            if (hasCardBeenUpdated == 1) {
                DbResponseWrapper.Success(data = true)
            } else {
                DbResponseWrapper.UserException(Exception("No card found to update"))
            }
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }
}
