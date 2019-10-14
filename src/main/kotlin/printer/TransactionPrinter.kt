package printer

import model.Transaction

typealias TransactionsFormatter = (List<Transaction>) -> List<String>
typealias Printer = (String) -> Unit

fun displayTransactions(
    transactions: List<Transaction>,
    formatter: TransactionsFormatter,
    printer: Printer
) {
    formatter.invoke(transactions).forEach(printer::invoke)
}
