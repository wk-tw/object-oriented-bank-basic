package printer

import model.Transaction
import org.junit.jupiter.api.Test
import java.math.BigDecimal.valueOf
import java.time.Instant.now







internal class PrinterFormatterKtTest {

    @Test
    fun formatTransactions() {
    }

    @Test
    fun `formatTransaction, should succeed`() {
        val transaction = Transaction("123123", valueOf(300.00), valueOf(300.00), now())

        println(formatTransaction(transaction))
    }


}