package service

import com.nhaarman.mockitokotlin2.*
import exception.AccountNotFoundException
import exception.NotEnoughFundsException
import exception.UnableToAddIntoDatabaseException
import model.Transaction
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
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
                executionDate = TODAY.minus(10, DAYS)
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-500),
                balance = BigDecimal(1500),
                executionDate = TODAY.minus(8, DAYS)
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-1000),
                balance = BigDecimal(500),
                executionDate = TODAY.minus(3, DAYS)
            )
        )

        private fun createFR6017569000704817168116U94() = listOf<Transaction>(
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(2000),
                balance = BigDecimal(2000),
                executionDate = TODAY.minus(10, DAYS)
            ),
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(-1000),
                balance = BigDecimal(1000),
                executionDate = TODAY.minus(8, DAYS)
            ),
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(-1000),
                balance = BigDecimal(0),
                executionDate = TODAY.minus(6, DAYS)
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
        "FR3217569000403186528461V35, 500, 500, 1000",
        "FR3217569000403186528461V35, 5000, 500, 5500",
        "FR6017569000704817168116U94, 5000, 0, 5000"
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

        val response = accountService.deposit(id, requestedAmount)
        verifyNoMoreInteractions(transactionRepository)
        assertThat(response).isEqualToComparingFieldByField(transaction)
    }

    @ParameterizedTest
    @CsvSource(
        "BE30131851926511, 50000",
        "BE30131851926511, 1000",
        "BE30131851926511, 25630"
    )
    fun `deposit, account not found, should throw`(id: String, requestedAmount: BigDecimal) {
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(null)

        Assertions.assertThatThrownBy { accountService.deposit(id, requestedAmount) }
            .isExactlyInstanceOf(AccountNotFoundException::class.java)
            .hasMessageContaining("Can not find transactions with accountId")
        verifyNoMoreInteractions(transactionRepository)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, 500, 500, 1000",
        "FR3217569000403186528461V35, 5000, 500, 5500",
        "FR6017569000704817168116U94, 5000, 0, 5000"
    )
    fun `deposit, unable to add transaction, should throw`(
        id: String,
        requestedAmount: BigDecimal,
        initialBalance: BigDecimal,
        newBalance: BigDecimal
    ) {
        val transaction = Transaction(id, requestedAmount, newBalance, TODAY)
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(initialBalance)
        Mockito.`when`(transactionRepository.add(transaction)).thenReturn(null)

        Assertions.assertThatThrownBy { accountService.deposit(id, requestedAmount) }
            .isExactlyInstanceOf(UnableToAddIntoDatabaseException::class.java)
            .hasMessageContaining("Unable to add")
        verifyNoMoreInteractions(transactionRepository)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, -500, 500, 0",
        "FR3217569000403186528461V35, -100, 500, 400",
        "FR3217569000403186528461V35, -50, 500, 450"
    )
    fun `withdraw, should succeed`(
        id: String,
        requestedAmount: BigDecimal,
        initialBalance: BigDecimal,
        expectedBalance: BigDecimal
    ) {
        val transaction = Transaction(id, requestedAmount, expectedBalance, TODAY)
        Mockito.`when`(transactionRepository.add(eq(transaction))).thenReturn(transaction)
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(initialBalance)

        val response = accountService.withdraw(id, requestedAmount)!!
        verifyNoMoreInteractions(transactionRepository)
        assertThat(response).isEqualToComparingFieldByField(transaction)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, -50000, 500",
        "FR3217569000403186528461V35, -1000, 500",
        "FR3217569000403186528461V35, -25630, 500"
    )
    fun `withdraw, not enough funds, should fail`(
        id: String,
        requestedAmount: BigDecimal,
        initialBalance: BigDecimal
    ) {
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(initialBalance)

        Assertions.assertThatThrownBy { accountService.withdraw(id, requestedAmount) }
            .isExactlyInstanceOf(NotEnoughFundsException::class.java)
            .hasMessage("Not enough funds.")
        verifyNoMoreInteractions(transactionRepository)
    }

    @ParameterizedTest
    @CsvSource(
        "BE30131851926511, -50000",
        "BE30131851926511, -1000",
        "BE30131851926511, -25630"
    )
    fun `withdraw, account not found, should throw`(id: String, requestedAmount: BigDecimal) {
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(null)

        Assertions.assertThatThrownBy { accountService.withdraw(id, requestedAmount) }
            .isExactlyInstanceOf(AccountNotFoundException::class.java)
            .hasMessageContaining("Can not find transactions with accountId")
        verifyNoMoreInteractions(transactionRepository)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, -500, 500, 0",
        "FR3217569000403186528461V35, -100, 500, 400",
        "FR3217569000403186528461V35, -50, 500, 450"
    )
    fun `withdraw, unable to add transaction, should throw`(
        id: String,
        requestedAmount: BigDecimal,
        initialBalance: BigDecimal,
        newBalance: BigDecimal
    ) {
        val transaction = Transaction(id, requestedAmount, newBalance, TODAY)
        Mockito.`when`(transactionRepository.getBalance(eq(id))).thenReturn(initialBalance)
        Mockito.`when`(transactionRepository.add(transaction)).thenReturn(null)

        Assertions.assertThatThrownBy { accountService.withdraw(id, requestedAmount) }
            .isExactlyInstanceOf(UnableToAddIntoDatabaseException::class.java)
            .hasMessageContaining("Unable to add")
        verifyNoMoreInteractions(transactionRepository)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "FR3217569000403186528461V35",
            "FR6017569000704817168116U94"
        ]
    )
    fun `displayOperations, should succeed`(accountId: String) {
        Mockito.`when`(transactionRepository.findByAccountId(eq(accountId))).thenReturn(getTransactionsById(accountId))
        val printFunc = mock<(List<Transaction>) -> Unit>()
        accountService.displayOperations(accountId, printFunc)
        verify(printFunc, times(1)).invoke(any())
        verifyNoMoreInteractions(transactionRepository)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "FR3217569000403186528461V35",
            "FR6017569000704817168116U94"
        ]
    )
    fun `displayOperations, emptyList, should succeed`(accountId: String) {
        Mockito.`when`(transactionRepository.findByAccountId(eq(accountId))).thenReturn(emptyList())
        val printFunc = mock<(List<Transaction>) -> Unit>()
        accountService.displayOperations(accountId, printFunc)
        verify(printFunc, times(1)).invoke(any())
        verifyNoMoreInteractions(transactionRepository)
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "FR3217569000403186528461V35",
            "FR6017569000704817168116U94"
        ]
    )
    fun `displayOperations, null, should throw`(accountId: String) {
        Mockito.`when`(transactionRepository.findByAccountId(eq(accountId))).thenReturn(null)
        val printFunc = mock<(List<Transaction>) -> Unit>()
        Assertions.assertThatThrownBy { accountService.displayOperations(accountId, printFunc) }
            .isExactlyInstanceOf(AccountNotFoundException::class.java)
            .hasMessageContaining("Can not find transactions with accountId")
        verifyNoMoreInteractions(transactionRepository)
    }

}