package printer

import model.Transaction
import java.math.BigDecimal.ZERO

fun formatTransactions(transactions: List<Transaction>): List<String> {
    val header = "|| %20s | %10s | %10s | %10s ||".format("DATE", "CREDIT", "DEBIT", "BALANCE")
    val formattedTransactions = transactions
        .sortedByDescending { it.executionDate }
        .map { formatTransaction(it) }
    return listOf(header, *formattedTransactions.toTypedArray())
}


private fun formatTransaction(transaction: Transaction) =
    if (transaction.amount > ZERO)
        "|| %20s | %,10.2f | %10s | %,10.2f ||"
            .format(transaction.executionDate, transaction.amount, "", transaction.balance)
    else
        "|| %20s | %10s | %,10.2f | %,10.2f ||"
            .format(transaction.executionDate, "", transaction.amount, transaction.balance)

