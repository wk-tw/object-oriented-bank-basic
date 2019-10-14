package printer

import model.Transaction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import service.AccountServiceTest
import java.math.BigDecimal
import java.time.temporal.ChronoUnit


internal class PrinterFormatterKtTest {
    companion object {
        fun createFR6017569000704817168116U94() = listOf<Transaction>(
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(2000),
                balance = BigDecimal(2000),
                date = AccountServiceTest.TODAY.minus(10, ChronoUnit.DAYS)
            ),
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(-1000),
                balance = BigDecimal(1000),
                date = AccountServiceTest.TODAY.minus(8, ChronoUnit.DAYS)
            ),
            Transaction(
                accountId = "FR6017569000704817168116U94",
                amount = BigDecimal(-1000),
                balance = BigDecimal(0),
                date = AccountServiceTest.TODAY.minus(6, ChronoUnit.DAYS)
            )
        )
    }

    @ParameterizedTest
    @CsvSource(
        "|| 2019-10-01 00H00 |   2000   |          |   2000   ||, 0",
        "|| 2019-10-03 00H00 |          |   -1000  |   1000   ||, 1",
        "|| 2019-10-05 00H00 |          |   -1000  |     0    ||, 2"
    )
    fun `formatTransaction, should succeed`(expectedString: String, index: Int) {
        assertThat(formatTransaction(createFR6017569000704817168116U94()[index]))
            .isEqualTo(expectedString)
    }

    @Test
    fun `formatTransactions, should succeed`() {
        assertThat(formatTransactions(createFR6017569000704817168116U94()))
            .isEqualTo(
                listOf(
                    "|| 2019-10-01 00H00 |   2000   |          |   2000   ||",
                    "|| 2019-10-03 00H00 |          |   -1000  |   1000   ||",
                    "|| 2019-10-05 00H00 |          |   -1000  |     0    ||"
                )
            )
    }

}