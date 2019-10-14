package printer

import model.Transaction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.math.BigDecimal
import java.time.Instant

internal class PrinterKtTest {
    companion object {
        private fun createTransactions() = listOf(
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(2000),
                balance = BigDecimal(2000),
                date = Instant.parse("2019-10-01T08:15:23Z")
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-500),
                balance = BigDecimal(1500),
                date = Instant.parse("2019-10-02T15:35:23Z")
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-1000),
                balance = BigDecimal(500),
                date = Instant.parse("2019-10-03T09:50:23Z")
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(200000),
                balance = BigDecimal(200500),
                date = Instant.parse("2019-10-05T23:15:23Z")
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-15000),
                balance = BigDecimal(185500),
                date = Instant.parse("2019-10-08T11:46:23Z")
            )
        )
    }

    @Test
    fun printStatement() {
        val outContent = ByteArrayOutputStream();
        System.setOut(PrintStream(outContent));

        printStatement(createTransactions())

        val expectedOutput =
            "||       DATE       |  CREDIT  |   DEBIT  |  BALANCE ||${System.lineSeparator()}" +
                    "|| 2019-10-01 08H15 |   2000   |          |   2000   ||${System.lineSeparator()}" +
                    "|| 2019-10-02 15H35 |          |   -500   |   1500   ||${System.lineSeparator()}" +
                    "|| 2019-10-03 09H50 |          |   -1000  |    500   ||${System.lineSeparator()}" +
                    "|| 2019-10-05 23H15 |  200000  |          |  200500  ||${System.lineSeparator()}" +
                    "|| 2019-10-08 11H46 |          |  -15000  |  185500  ||${System.lineSeparator()}"

        assertThat(outContent.toString()).isEqualTo(expectedOutput)
    }
}