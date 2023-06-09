package com.adikmt.taskBoard.repositories.cards

import com.adikmt.taskBoard.dtos.common.mappers.toCardResponse
import com.adikmt.taskBoard.dtos.common.wrappers.DbResponseWrapper
import com.adikmt.taskBoard.dtos.requests.CardRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateBucketRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateRequest
import com.adikmt.taskBoard.dtos.requests.CardUpdateUserRequest
import com.adikmt.taskBoard.dtos.responses.CardResponse
import java.time.LocalDateTime
import jooq.generated.tables.records.CardsRecord
import jooq.generated.tables.references.BOARDS
import jooq.generated.tables.references.BUCKETS
import jooq.generated.tables.references.CARDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactive.asFlow
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux

class CardRepositoryImpl @Autowired constructor(private val context: DSLContext) : CardRepository {
    override fun createCard(cardRequest: CardRequest, userId: Int): DbResponseWrapper<Int> {
        return try {
            val cardId = context.insertInto<CardsRecord>(CARDS)
                .set(CARDS.CARD_TITLE, cardRequest.title)
                .set(CARDS.CARD_DESC, cardRequest.description)
                .set(CARDS.IS_CARD_ARCHIVED, cardRequest.isCardArchived)
                .set(CARDS.BUCKET_ID, cardRequest.bucketId)
                .set(CARDS.USER_ASSIGNED_ID, userId)
                .set(CARDS.CARD_START_DATE, LocalDateTime.parse(cardRequest.startDate))
                .set(CARDS.CARD_END_DATE, LocalDateTime.parse(cardRequest.endDate))
                .onDuplicateKeyIgnore()
                .returningResult<Int>(CARDS.ID)
                .execute()

            DbResponseWrapper.Success(
                data = cardId
            )
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun getAllCards(boardId: Int): Flow<DbResponseWrapper<CardResponse>> {
        return try {
            Flux.from<DbResponseWrapper<CardResponse>> {
                context.select()
                    .from(BOARDS)
                    .innerJoin(BUCKETS).on(BOARDS.ID.eq(BUCKETS.BOARD_ID))
                    .innerJoin(CARDS).on(BUCKETS.ID.eq(CARDS.BUCKET_ID))
                    .where(BOARDS.ID.eq(boardId).and(CARDS.IS_CARD_ARCHIVED.isFalse))
                    .orderBy(CARDS.CARD_END_DATE)
                    .fetchStreamInto(CardsRecord::class.java)
                    .map {
                        it.toCardResponse()
                    }
                    .map { card ->
                        DbResponseWrapper.Success(data = card)
                    }
            }
                .distinctUntilChanged()
                .asFlow()
        } catch (e: Exception) {
            flow {
                DbResponseWrapper.ServerException(
                    exception = e
                )
            }
        }
    }

    override fun getAllCardsAssignedToUserById(
        userId: Int,
        boardId: Int
    ): Flow<DbResponseWrapper<CardResponse>> {
        return try {
            Flux.from<DbResponseWrapper<CardResponse>> {
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
                    .fetchStreamInto(CardsRecord::class.java)
                    .map {
                        it.toCardResponse()
                    }
                    .map { card ->
                        DbResponseWrapper.Success(data = card)
                    }
            }
                .distinctUntilChanged()
                .asFlow()
        } catch (e: Exception) {
            flow {
                DbResponseWrapper.ServerException(
                    exception = e
                )
            }
        }
    }

    override fun updateCardDetails(cardRequest: CardUpdateRequest): DbResponseWrapper<Int> {
        return try {
            val id = context.update(CARDS)
                .set(CARDS.IS_CARD_ARCHIVED, cardRequest.isCardArchived)
                .set(CARDS.CARD_DESC, cardRequest.description)
                .set(CARDS.CARD_END_DATE, LocalDateTime.parse(cardRequest.endDate))
                .where(CARDS.ID.eq(cardRequest.id))
                .returningResult<Int>(CARDS.ID)
                .execute()

            DbResponseWrapper.Success(data = id)
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun updateCardBucket(cardUpdateBucketRequest: CardUpdateBucketRequest): DbResponseWrapper<Int> {
        return try {
            val id = context.update(CARDS)
                .set(CARDS.BUCKET_ID, cardUpdateBucketRequest.bucketId)
                .where(CARDS.ID.eq(cardUpdateBucketRequest.id))
                .returningResult<Int>(CARDS.ID)
                .execute()

            DbResponseWrapper.Success(data = id)
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }

    override fun assignCardToAnotherUser(cardUpdateUserRequest: CardUpdateUserRequest): DbResponseWrapper<Int> {
        return try {
            val id = context.update(CARDS)
                .set(CARDS.USER_ASSIGNED_ID, cardUpdateUserRequest.newUserId)
                .where(CARDS.ID.eq(cardUpdateUserRequest.id))
                .returningResult<Int>(CARDS.ID)
                .execute()

            DbResponseWrapper.Success(data = id)
        } catch (e: Exception) {
            DbResponseWrapper.ServerException(
                exception = e
            )
        }
    }
}
