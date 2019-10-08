package service

import client.TransactionClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito
import org.mockito.Mockito.mock
import utils.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TransactionServiceTest {
    private lateinit var transactionClient: TransactionClient
    private lateinit var balanceService: BalanceService
    private lateinit var transactionService: TransactionService


    @BeforeAll
    fun setup() {
        this.transactionClient = mock(TransactionClient::class.java)
        this.balanceService = BalanceService(transactionClient)
        this.transactionService = TransactionService(balanceService, transactionClient)
        Mockito.`when`(transactionClient.findById("FR3217569000403186528461V35"))
            .thenReturn(createFR3217569000403186528461V35())
        Mockito.`when`(transactionClient.findById("FR6017569000704817168116U94"))
            .thenReturn(createFR6017569000704817168116U94())
        Mockito.`when`(transactionClient.findById("FR4930003000302945844589B40"))
            .thenReturn(createFR4930003000302945844589B40())
        Mockito.`when`(transactionClient.save(createAfterDepositTransaction()))
            .thenReturn(createAfterDepositTransaction())
        Mockito.`when`(transactionClient.save(createAfterWithdrawalTransaction()))
            .thenReturn(createAfterWithdrawalTransaction())
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, 4",
        "FR6017569000704817168116U94, 4",
        "FR4930003000302945844589B40, 0"
    )
    fun `checkOperations, should succeed`(accountId: String, size: Int) {
        assertThat(transactionService.checkOperations(accountId)).hasSize(size)
    }

    @Test
    fun `deposit, should succeed`() {
        assertThat(transactionService.deposit(createDepositTransaction()))
            .isEqualToComparingFieldByField(createAfterDepositTransaction())
    }

    @Test
    fun withdrawal() {
        assertThat(transactionService.withdrawal(createWithdrawalTransaction()))
            .isEqualToComparingFieldByField(createAfterWithdrawalTransaction())
    }
}