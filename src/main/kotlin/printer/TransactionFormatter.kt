package printer

import model.Transaction
import java.math.BigDecimal.ZERO

fun formatTransactions(transactions: List<Transaction>): List<String> {
    val header = "|| %18s | %10s | %10s | %10s ||".format("DATE", "CREDIT", "DEBIT", "BALANCE")
    val formattedTransactions = transactions
        .sortedBy { it.date }
        .map { formatTransaction(it) }
    return listOf(header, *formattedTransactions.toTypedArray())
}


private fun formatTransaction(transaction: Transaction) =
    transaction
        .takeIf { it.amount > ZERO }
        ?.let {
            "|| %18s | %10.2f | %10s | %10.2f ||".format(it.date, it.amount, "", it.balance)
        }
        ?: "|| %18s | %10s | %10.2f | %10.2f ||".format(transaction.date, "", transaction.amount, transaction.balance)

