package printer

import model.Transaction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant

internal class TransactionPrinterKtTest {
    companion object {
        private fun createTransactions() = listOf(
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(2000),
                balance = BigDecimal(2000),
                executionDate = Instant.parse("2019-10-01T08:15:23Z")
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-500),
                balance = BigDecimal(1500),
                executionDate = Instant.parse("2019-10-02T15:35:23Z")
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-1000),
                balance = BigDecimal(500),
                executionDate = Instant.parse("2019-10-03T09:50:23Z")
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(200000),
                balance = BigDecimal(200500),
                executionDate = Instant.parse("2019-10-05T23:15:23Z")
            ),
            Transaction(
                accountId = "FR3217569000403186528461V35",
                amount = BigDecimal(-15000),
                balance = BigDecimal(185500),
                executionDate = Instant.parse("2019-10-08T11:46:23Z")
            )
        )
    }

    @Test
    fun printStatement() {
        val printStream = mutableListOf<String>()
        fun printer(str: String) {
            printStream.add(str)
        }

        fun formatter(transactions: List<Transaction>) =
            transactions.map(Transaction::amount).map(BigDecimal::toPlainString)
        transactionsPrinter(createTransactions(), ::formatter, ::printer)
        assertThat(formatter(createTransactions())).isEqualTo(printStream)
    }
}