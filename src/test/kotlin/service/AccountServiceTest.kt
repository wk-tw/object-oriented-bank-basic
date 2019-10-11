package service

import com.nhaarman.mockitokotlin2.eq
import exception.NotEnoughFundsException
import model.Transaction
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import repository.TransactionRepository
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit.DAYS

@ExtendWith(MockitoExtension::class)
internal class AccountServiceTest {
    companion object {
        val TODAY: Instant = Instant.parse("2019-10-11T00:00:00Z")

        fun getTransactionsById(accountId: String): List<Transaction> =
            when (accountId) {
                "FR3217569000403186528461V35" -> createFR3217569000403186528461V35()
                "FR6017569000704817168116U94" -> createFR6017569000704817168116U94()
                "FR4930003000302945844589B40" -> createFR4930003000302945844589B40()
                else -> throw IllegalStateException("Unable to find transactions with id $accountId")
            }

        private fun createFR3217569000403186528461V35() = listOf<Transaction>(
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(2000),
                balance = BigDecimal(2000),
                date = TODAY.minus(10, DAYS)
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-3000),
                balance = BigDecimal(2000),
                date = TODAY.minus(9, DAYS)
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-500),
                balance = BigDecimal(1500),
                date = TODAY.minus(8, DAYS)
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-1000),
                balance = BigDecimal(500),
                date = TODAY.minus(3, DAYS)
            )
        )

        private fun createFR6017569000704817168116U94() = listOf<Transaction>(
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(2000),
                balance = BigDecimal(2000),
                date = TODAY.minus(10, DAYS)
            ),
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(-1000),
                balance = BigDecimal(1000),
                date = TODAY.minus(8, DAYS)
            ),
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(-1000),
                balance = BigDecimal(0),
                date = TODAY.minus(6, DAYS)
            ),
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(-1000),
                balance = BigDecimal(0),
                date = TODAY
            )
        )

        private fun createFR4930003000302945844589B40() = emptyList<Transaction>()
    }

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    private lateinit var accountService: AccountService

    @BeforeEach
    fun setup() {
        accountService = AccountService(transactionRepository, Clock.fixed(TODAY, ZoneOffset.UTC))
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, 4",
        "FR6017569000704817168116U94, 4",
        "FR4930003000302945844589B40, 0"
    )
    fun `checkOperations, should succeed`(accountId: String, size: Int) {
        Mockito.`when`(transactionRepository.findByAccountId(eq(accountId))).thenReturn(getTransactionsById(accountId))

        assertThat(accountService.checkOperations(accountId)).hasSize(size)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, 500, 500, 1000",
        "FR3217569000403186528461V35, 5000, 500, 5500"
    )
    fun `deposit, should succeed`(
        id: String,
        requestedAmount: BigDecimal,
        initialBalance: BigDecimal,
        expectedBalance: BigDecimal
    ) {
        val transaction = Transaction(id, requestedAmount, expectedBalance, TODAY)
        Mockito.`when`(transactionRepository.add(eq(transaction))).thenReturn(transaction)
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(initialBalance)

        val response = accountService.deposit(id, requestedAmount)!!
        assertThat(response.accountId).isEqualTo(id)
        assertThat(response.amount).isEqualTo(requestedAmount)
        assertThat(response.balance).isEqualTo(expectedBalance)
        assertThat(response.date).isEqualTo(TODAY)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, -500, 500, 0",
        "FR3217569000403186528461V35, -100, 500, 400",
        "FR3217569000403186528461V35, -50, 500, 450"
    )
    fun `withdrawal, should succeed`(
        id: String,
        requestedAmount: BigDecimal,
        initialBalance: BigDecimal,
        expectedBalance: BigDecimal
    ) {
        val transaction = Transaction(id, requestedAmount, expectedBalance, TODAY)
        Mockito.`when`(transactionRepository.add(eq(transaction))).thenReturn(transaction)
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(initialBalance)

        val response = accountService.withdraw(id, requestedAmount)!!
        assertThat(response.accountId).isEqualTo(id)
        assertThat(response.amount).isEqualTo(requestedAmount)
        assertThat(response.balance).isEqualTo(expectedBalance)
        assertThat(response.date).isEqualTo(TODAY)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, -50000, 500",
        "FR3217569000403186528461V35, -1000, 500",
        "FR3217569000403186528461V35, -25630, 500"
    )
    fun `withdrawal, should fail`(id: String, requestedAmount: BigDecimal, initialBalance: BigDecimal) {
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(initialBalance)

        Assertions.assertThatThrownBy { accountService.withdraw(id, requestedAmount) }
            .isExactlyInstanceOf(NotEnoughFundsException::class.java)
            .hasMessage("Not enough funds.")
    }
}