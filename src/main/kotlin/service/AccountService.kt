package service

import exception.NotEnoughFundsException
import model.Transaction
import printer.formatTransactions
import repository.TransactionRepository
import validator.checkDepositTransaction
import validator.checkWithdrawalTransaction
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant.now

/**
 * US 1: In order to save money, as a bank client, I want to make a deposit in my account
 * @see [withdraw]
 * US 2: In order to retrieve some or all of my savings, as a bank client, I want to make a withdrawal from my account
 * @see [deposit]
 * US 3: In order to check my operations, as a bank client, I want to see the history (operation, date, amount, balance) of my operations
 * @see [displayOperations]
 */
class AccountService(
    private val transactionRepository: TransactionRepository,
    private val clock: Clock? = Clock.systemUTC()
) {

    fun withdraw(accountId: String, amount: BigDecimal): Transaction? {
        checkWithdrawalTransaction(amount)
        return transactionRepository.getBalance(accountId)?.let { actualBalance ->
            if (isEnoughFunds(amount, actualBalance)) {
                val newBalance = actualBalance.plus(amount)
                val newTransaction = Transaction(accountId, amount, newBalance, now(clock))
                transactionRepository.add(newTransaction)
            } else throw NotEnoughFundsException("Not enough funds.")
        }
    }

    private fun isEnoughFunds(requested: BigDecimal, actual: BigDecimal) = requested.abs() <= actual

    fun deposit(accountId: String, amount: BigDecimal): Transaction? {
        checkDepositTransaction(amount)
        return transactionRepository.getBalance(accountId)?.let { actualBalance ->
            val newBalance = actualBalance.plus(amount)
            val newTransaction = Transaction(accountId, amount, newBalance, now(clock))
            transactionRepository.add(newTransaction)
        }
    }

    fun displayOperations(accountId: String) =
        transactionRepository.findByAccountId(accountId)?.let {
            printer.transactionsPrinter(it, ::formatTransactions, System.out::println)
        }

}
