package printer

import model.Transaction
import printer.PrinterFormatter.Companion.BALANCE_MAX_FIELD_LENGTH
import printer.PrinterFormatter.Companion.CREDIT_MAX_FIELD_LENGTH
import printer.PrinterFormatter.Companion.DATE_MAX_FIELD_LENGTH
import printer.PrinterFormatter.Companion.DEBIT_MAX_FIELD_LENGTH
import utils.pad
import java.math.BigDecimal.ZERO

class PrinterFormatter {
    companion object {
        const val DATE_MAX_FIELD_LENGTH = 18
        const val CREDIT_MAX_FIELD_LENGTH = 10
        const val DEBIT_MAX_FIELD_LENGTH = 10
        const val BALANCE_MAX_FIELD_LENGTH = 10
    }
}

fun formatHeader(date: String, credit: String, debit: String, balance: String) =
    formatLine(
        date.pad(DATE_MAX_FIELD_LENGTH),
        credit.pad(CREDIT_MAX_FIELD_LENGTH),
        debit.pad(DEBIT_MAX_FIELD_LENGTH),
        balance.pad(BALANCE_MAX_FIELD_LENGTH)
    )

fun formatTransactions(transactions: List<Transaction>): List<String> =
    transactions
        .sortedBy { it.date }
        .map { formatTransaction(it) }

fun formatTransaction(transaction: Transaction) =
    transaction
        .takeIf { it.amount > ZERO }
        ?.let { formatCreditorLine(transaction) }
        ?: formatDebtorLine(transaction)

private fun formatDebtorLine(transaction: Transaction): String {
    return formatLine(
        transaction.date.pad(DATE_MAX_FIELD_LENGTH),
        "".pad(CREDIT_MAX_FIELD_LENGTH),
        transaction.amount.pad(DEBIT_MAX_FIELD_LENGTH),
        transaction.balance.pad(BALANCE_MAX_FIELD_LENGTH)
    )
}

private fun formatCreditorLine(transaction: Transaction): String {
    return formatLine(
        transaction.date.pad(DATE_MAX_FIELD_LENGTH),
        transaction.amount.pad(CREDIT_MAX_FIELD_LENGTH),
        "".pad(DEBIT_MAX_FIELD_LENGTH),
        transaction.balance.pad(BALANCE_MAX_FIELD_LENGTH)
    )
}

private fun formatLine(paddedDate: String, paddedCredit: String, paddedDebit: String, paddedBalance: String) =
    "||$paddedDate|$paddedCredit|$paddedDebit|$paddedBalance||"
