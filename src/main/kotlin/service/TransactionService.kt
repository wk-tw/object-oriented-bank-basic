package service

import client.TransactionClient
import model.Transaction
import model.TransactionStatus.REJECTED
import model.TransactionStatus.SUCCESS
import validator.checkDepositTransaction
import validator.checkWithdrawalTransaction
import java.math.BigDecimal

/**
 * US 1: In order to save money, as a bank client, I want to make a deposit in my account
 * @see [withdraw]
 * US 2: In order to retrieve some or all of my savings, as a bank client, I want to make a withdrawal from my account
 * @see [deposit]
 * US 3: In order to check my operations, as a bank client, I want to see the history (operation, date, amount, balance) of my operations
 * @see [checkOperations]
 */
class TransactionService(
    private val balanceService: BalanceService,
    private val transactionClient: TransactionClient
) {
    fun withdraw(transaction: Transaction): Transaction {
        checkWithdrawalTransaction(transaction)
        val actualBalance = balanceService.getBalance(transaction.accountId)
        return transaction
            .takeIf { isEnoughFonds(it.money, actualBalance) }
            ?.let {
                val newBalance = actualBalance.plus(it.money)
                val newTransaction = it.copy(balance = newBalance, transactionStatus = SUCCESS)
                transactionClient.save(newTransaction)
            }
            ?: transaction.apply { this.transactionStatus = REJECTED }
    }


    private fun isEnoughFonds(requested: BigDecimal, actual: BigDecimal) = requested.abs() <= actual


    fun deposit(transaction: Transaction): Transaction {
        checkDepositTransaction(transaction)
        val newBalance = balanceService.getBalance(transaction.accountId).plus(transaction.money)
        val newTransaction = transaction.copy(balance = newBalance, transactionStatus = SUCCESS)
        return transactionClient.save(newTransaction)
    }


    fun checkOperations(accountId: String): List<Transaction> = transactionClient.findById(accountId)
}
