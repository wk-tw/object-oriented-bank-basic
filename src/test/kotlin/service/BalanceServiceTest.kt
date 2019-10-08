package service

import client.TransactionClient
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import utils.createFR3217569000403186528461V35
import utils.createFR4930003000302945844589B40
import utils.createFR6017569000704817168116U94
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BalanceServiceTest {

    private lateinit var transactionClient: TransactionClient
    private lateinit var balanceService: BalanceService

    @BeforeAll
    fun setup() {
        this.transactionClient = Mockito.mock(TransactionClient::class.java)
        this.balanceService = BalanceService(transactionClient)
        Mockito.`when`(transactionClient.findById("FR3217569000403186528461V35"))
            .thenReturn(createFR3217569000403186528461V35())
        Mockito.`when`(transactionClient.findById("FR6017569000704817168116U94"))
            .thenReturn(createFR6017569000704817168116U94())
        Mockito.`when`(transactionClient.findById("FR4930003000302945844589B40"))
            .thenReturn(createFR4930003000302945844589B40())
    }

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, 500",
        "FR6017569000704817168116U94, 0"
    )
    fun `getBalance, should succeed`(accountId: String, amount: BigDecimal) {
        assertThat(balanceService.getBalance(accountId)).isEqualTo(amount)
    }

    @ParameterizedTest
    @ValueSource(strings = ["FR4930003000302945844589B40"])
    fun `getBalance, empty list,should throw`(accountId: String) {
        assertThatThrownBy { balanceService.getBalance(accountId) }
            .isExactlyInstanceOf(IllegalStateException::class.java)
            .hasMessage("Unable to retrieve balance")
    }
}