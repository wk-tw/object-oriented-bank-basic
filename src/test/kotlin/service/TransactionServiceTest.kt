package service

import client.TransactionClient
import com.nhaarman.mockitokotlin2.eq
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import utils.*

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

    @Test
    fun `deposit, should succeed`() {
        val accountId = "FR3217569000403186528461V35"
        Mockito.`when`(transactionClient.findById(eq(accountId)))
            .thenReturn(getTransactionsById(accountId))
        Mockito.`when`(transactionClient.save(eq(createAfterDepositTransaction())))
            .thenReturn(createAfterDepositTransaction())

        assertThat(transactionService.deposit(createDepositTransaction()))
            .isEqualToComparingFieldByField(createAfterDepositTransaction())
    }

    @Test
    fun withdrawal() {
        val accountId = "FR3217569000403186528461V35"
        Mockito.`when`(transactionClient.findById(eq(accountId)))
            .thenReturn(getTransactionsById(accountId))
        Mockito.`when`(transactionClient.save(eq(createAfterWithdrawalTransaction())))
            .thenReturn(createAfterWithdrawalTransaction())

        assertThat(transactionService.withdraw(createWithdrawalTransaction()))
            .isEqualToComparingFieldByField(createAfterWithdrawalTransaction())
    }
}