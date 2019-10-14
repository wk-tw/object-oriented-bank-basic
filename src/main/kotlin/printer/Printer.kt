package printer

import model.Transaction

fun printStatement(transactions: List<Transaction>) {
    listOf(
        formatHeader("DATE", "CREDIT", "DEBIT", "BALANCE"),
        *formatTransactions(transactions).toTypedArray()
    )
        .forEach(System.out::println)
}
