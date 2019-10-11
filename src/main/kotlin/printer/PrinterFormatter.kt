package printer

import model.Transaction
import java.math.BigDecimal.ZERO
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

const val FIRSTLIGNE = "||  DATE  |  CREDIT  |  DEBIT  |  BALANCE  ||"

const val PADDING_SIZE = 30

fun formatTransactions(transactions: List<Transaction>) =
    transactions.map { "|| ${it.date} | ${it.amount} | | ${it.balance}" }

fun formatTransaction(t: Transaction) =
    t.takeIf { it.amount > ZERO }
        ?.let { "||${it.date.toDefault().withPadding()}|${it.amount.withPadding()}|${"".withPadding()}|${it.balance.withPadding()}||" }
        ?: "||${t.date.toDefault().withPadding()}|${"".withPadding()}|${t.amount.withPadding()}|${t.balance.withPadding()}||"

private fun Any.withPadding() : String {
    return this.toString()
        .padStart(PADDING_SIZE - this.toString().length)
        .padEnd(PADDING_SIZE - this.toString().length)
}

fun Instant.toDefault(): String = DateTimeFormatter
    .ofPattern("YYYY-MM-dd HH'H'mm")
    .withZone(ZoneOffset.UTC)
    .format(this)
