package validator

import model.Transaction
import model.TransactionType
import java.math.BigDecimal

fun checkWithdrawalTransaction(transaction: Transaction) {
    check(transaction.money < BigDecimal.ZERO) { "Amount ${transaction.money} must be negative." }
    check(transaction.transactionType == TransactionType.WITHDRAWAL) { "Type ${transaction.transactionType} must be ${TransactionType.WITHDRAWAL}." }
}

fun checkDepositTransaction(transaction: Transaction) {
    check(transaction.money > BigDecimal.ZERO) { "Amount ${transaction.money} must not be negative." }
    check(transaction.transactionType == TransactionType.DEPOSIT) { "Type ${transaction.transactionType} must be ${TransactionType.DEPOSIT}." }
}