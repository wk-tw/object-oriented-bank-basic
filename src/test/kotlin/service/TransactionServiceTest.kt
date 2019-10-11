package service

import client.TransactionClient
import com.nhaarman.mockitokotlin2.eq
import exception.NotEnoughFondsException
import model.Transaction
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import utils.getTransactionsById
import java.math.BigDecimal
import java.time.LocalDate.now

@ExtendWith(MockitoExtension::class)
internal class TransactionServiceTest {

    @Mock
    private lateinit var transactionClient: TransactionClient

    @InjectMocks
    private lateinit var balanceService: BalanceService

    private lateinit var transactionService: TransactionService

    @BeforeEach
    fun setup() {
        transactionService = TransactionService(balanceService, transactionClient)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, 4",
        "FR6017569000704817168116U94, 4",
        "FR4930003000302945844589B40, 0"
    )
    fun `checkOperations, should succeed`(accountId: String, size: Int) {
        Mockito.`when`(transactionClient.findById(eq(accountId)))
            .thenReturn(getTransactionsById(accountId))
        assertThat(transactionService.checkOperations(accountId)).hasSize(size)
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, 500, 1000",
        "FR3217569000403186528461V35, 5000, 5500"
    )
    fun `deposit, should succeed`(id: String, requestedAmount: BigDecimal, expectedBalance: BigDecimal) {
        val transaction = Transaction(id, requestedAmount, expectedBalance, now())
        Mockito.`when`(transactionClient.findById(eq(id)))
            .thenReturn(getTransactionsById(id))
        Mockito.`when`(transactionClient.save(eq(transaction)))
            .thenReturn(transaction)

        val response = transactionService.deposit(id, requestedAmount)
        assertThat(response).isNotNull
        assertThat(response.accountId).isEqualTo(id)
        assertThat(response.money).isEqualTo(requestedAmount)
        assertThat(response.balance).isEqualTo(expectedBalance)
        assertThat(response.requestedExecutionDate).isEqualTo(now())
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, -500, 0",
        "FR3217569000403186528461V35, -100, 400",
        "FR3217569000403186528461V35, -50, 450"
    )
    fun `withdrawal, should succeed`(id: String, requestedAmount: BigDecimal, expectedBalance: BigDecimal) {
        val transaction = Transaction(id, requestedAmount, expectedBalance, now())
        Mockito.`when`(transactionClient.findById(eq(id)))
            .thenReturn(getTransactionsById(id))
        Mockito.`when`(transactionClient.save(eq(transaction)))
            .thenReturn(transaction)

        val response = transactionService.withdraw(id, requestedAmount)
        assertThat(response).isNotNull
        assertThat(response.accountId).isEqualTo(id)
        assertThat(response.money).isEqualTo(requestedAmount)
        assertThat(response.balance).isEqualTo(expectedBalance)
        assertThat(response.requestedExecutionDate).isEqualTo(now())
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, -50000",
        "FR3217569000403186528461V35, -1000",
        "FR3217569000403186528461V35, -25630"
    )
    fun `withdrawal, should fail`(id: String, requestedAmount: BigDecimal) {
        Mockito.`when`(transactionClient.findById(eq(id)))
            .thenReturn(getTransactionsById(id))
        Assertions.assertThatThrownBy { transactionService.withdraw(id, requestedAmount) }
            .isExactlyInstanceOf(NotEnoughFondsException::class.java)
            .hasMessage("Not enough fonds.")
    }
}