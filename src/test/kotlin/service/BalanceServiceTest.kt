package service

import client.TransactionClient
import com.nhaarman.mockitokotlin2.eq
import exception.BalanceNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import utils.getTransactionsById
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
internal class BalanceServiceTest {

    @Mock
    private lateinit var transactionClient: TransactionClient

    @InjectMocks
    private lateinit var balanceService: BalanceService

    @ParameterizedTest
    @CsvSource(
        "FR3217569000403186528461V35, 500",
        "FR6017569000704817168116U94, 0"
    )
    fun `getBalance, should succeed`(accountId: String, amount: BigDecimal) {
        Mockito.`when`(transactionClient.findById(eq(accountId)))
            .thenReturn(getTransactionsById(accountId))

        assertThat(balanceService.getBalance(accountId)).isEqualTo(amount)
    }

    @ParameterizedTest
    @ValueSource(strings = ["FR4930003000302945844589B40"])
    fun `getBalance, empty list,should throw`(accountId: String) {
        Mockito.`when`(transactionClient.findById(eq(accountId)))
            .thenReturn(getTransactionsById(accountId))

        assertThatThrownBy { balanceService.getBalance(accountId) }
            .isExactlyInstanceOf(BalanceNotFoundException::class.java)
            .hasMessage("Unable to retrieve balance")
    }
}