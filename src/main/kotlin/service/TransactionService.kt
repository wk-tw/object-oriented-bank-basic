package service

import client.TransactionClient
import exception.NotEnoughFondsException
import model.Transaction
import validator.checkDepositTransaction
import validator.checkWithdrawalTransaction
import java.math.BigDecimal
import java.time.LocalDate.now

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

    fun withdraw(accountId: String, amount: BigDecimal): Transaction {
        checkWithdrawalTransaction(amount)
        val actualBalance = balanceService.getBalance(accountId)
        return amount
            .takeIf { isEnoughFonds(it, actualBalance) }
            ?.let {
                val newBalance = actualBalance.plus(it)
                val newTransaction = Transaction(accountId, amount, newBalance, now())
                transactionClient.save(newTransaction)
            }
            ?: throw NotEnoughFondsException("Not enough fonds.")
    }

    private fun isEnoughFonds(requested: BigDecimal, actual: BigDecimal) = requested.abs() <= actual

    fun deposit(accountId: String, amount: BigDecimal): Transaction {
        checkDepositTransaction(amount)
        val newBalance = balanceService.getBalance(accountId).plus(amount)
        val newTransaction = Transaction(accountId, amount, newBalance, now())
        return transactionClient.save(newTransaction)
    }

    fun checkOperations(accountId: String): List<Transaction> = transactionClient.findById(accountId)
}
