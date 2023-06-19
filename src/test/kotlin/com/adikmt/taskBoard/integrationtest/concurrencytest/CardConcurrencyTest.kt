package com.adikmt.taskBoard.integrationtest.concurrencytest

import com.adikmt.taskBoard.integrationtest.setupDSL
import jooq.generated.tables.records.CardsRecord
import jooq.generated.tables.references.CARDS
import org.jooq.DSLContext
import org.jooq.exception.DataChangedException
import org.jooq.impl.DSL
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate

/**
 * Heavily inspired from [jOOQ masterclass github](https://github.com/PacktPublishing/jOOQ-Masterclass/blob/master/Chapter19/MySQL/Maven/Testing/Testcontainers/src/test/java/com/classicmodels/test/ClassicmodelsIT.java)
 */

@JooqTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardConcurrencyTest {

    lateinit var context: DSLContext

    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate

    @BeforeAll
    fun setup() {
        context = setupDSL()
        context.settings()
            .withUpdateRecordTimestamp(true)
            .withExecuteWithOptimisticLocking(true)
            .withExecuteWithOptimisticLockingExcludeUnversioned(true)

        transactionTemplate.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    fun `check card update concurrency`() {
        assertThrows<DataChangedException>(message = "Database rows have been updated") {
            context.transaction { transaction ->
                val record1 = DSL.using(transaction).select()
                    .from(CARDS)
                    .where(CARDS.ID.eq(1))
                    .fetchSingleInto(CardsRecord::class.java)

                DSL.using(transaction).transaction { innerTransaction ->
                    val record2 = DSL.using(innerTransaction).select()
                        .from(CARDS)
                        .where(CARDS.ID.eq(1))
                        .fetchSingleInto(CardsRecord::class.java)

                    record2.set(CARDS.CARD_TITLE, "kjnefkjnef")
                    record2.set(CARDS.CARD_DESC, "kjndskjndksjndskjnfekjnfe kjnefkjnef")
                    record2.store()
                }

                record1.set(CARDS.CARD_TITLE, "iuybweuhbewjhwjbhjhbe")
                record1.store()
            }
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    fun `change bucket concurrency test`() {
        assertThrows<DataChangedException>(message = "Database rows have been updated") {
            context.transaction { transaction ->
                val record1 = DSL.using(transaction).select()
                    .from(CARDS)
                    .where(CARDS.ID.eq(1))
                    .fetchSingleInto(CardsRecord::class.java)

                DSL.using(transaction).transaction { innerTransaction ->
                    val record2 = DSL.using(innerTransaction).select()
                        .from(CARDS)
                        .where(CARDS.ID.eq(1))
                        .fetchSingleInto(CardsRecord::class.java)

                    record2.set(CARDS.BUCKET_ID, 2)
                    record2.store()
                }

                record1.set(CARDS.BUCKET_ID, 3)
                record1.store()
            }
        }
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    fun `change assignee concurrency test`() {
        assertThrows<DataChangedException>(message = "Database rows have been updated") {
            context.transaction { transaction ->
                val record1 = DSL.using(transaction).select()
                    .from(CARDS)
                    .where(CARDS.ID.eq(1))
                    .fetchSingleInto(CardsRecord::class.java)

                DSL.using(transaction).transaction { innerTransaction ->
                    val record2 = DSL.using(innerTransaction).select()
                        .from(CARDS)
                        .where(CARDS.ID.eq(1))
                        .fetchSingleInto(CardsRecord::class.java)

                    record2.set(CARDS.USER_ASSIGNED_ID, 2)
                    record2.store()
                }

                record1.set(CARDS.USER_ASSIGNED_ID, 3)
                record1.store()
            }
        }
    }
}