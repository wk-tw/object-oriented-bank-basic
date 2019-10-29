package service

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import model.Transaction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import printer.formatTransactions
import repository.TransactionRepository
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit.DAYS


@ExtendWith(MockitoExtension::class)
internal class AccountServiceAcceptanceTest {
    companion object {
        val TODAY: Instant = Instant.parse("2019-10-11T00:00:00Z")

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
    }

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    private lateinit var accountService: AccountService

    private lateinit var outContent: ByteArrayOutputStream

    @BeforeEach
    fun setup() {
        accountService = AccountService(transactionRepository, Clock.fixed(TODAY, ZoneOffset.UTC))
        outContent = ByteArrayOutputStream()
        System.setOut(PrintStream(outContent))
    }

    @AfterEach
    fun endUp() {
        System.setOut(System.out)
    }


    @Test
    fun `displayOperations, acceptance test, should succeed`() {
        val accountId = "FR3217569000403186528461V35"
        val transactions = createFR3217569000403186528461V35()

        Mockito.`when`(transactionRepository.findByAccountId(eq(accountId))).thenReturn(transactions)

        fun printFunc(transactions: List<Transaction>) = formatTransactions(transactions).forEach(System.out::println)

        accountService.displayOperations(accountId, ::printFunc)
        verifyNoMoreInteractions(transactionRepository)

        val expectedString =
            "||                 DATE |     CREDIT |      DEBIT |    BALANCE ||${System.lineSeparator()}" +
                    "|| 2019-10-08T00:00:00Z |            |  -1 000,00 |     500,00 ||${System.lineSeparator()}" +
                    "|| 2019-10-03T00:00:00Z |            |    -500,00 |   1 500,00 ||${System.lineSeparator()}" +
                    "|| 2019-10-01T00:00:00Z |   2 000,00 |            |   2 000,00 ||${System.lineSeparator()}"

        assertThat(outContent.toString()).isEqualTo(expectedString)
    }


}