package service

import exception.AccountNotFoundException
import exception.NotEnoughFundsException
import exception.UnableToAddIntoDatabaseException
import model.Transaction
import repository.TransactionRepository
import validator.checkDepositAmount
import validator.checkWithdrawalAmount
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

    fun deposit(accountId: String, amount: BigDecimal): Transaction {
        checkDepositAmount(amount)
        val actualBalance = transactionRepository.getBalance(accountId)
            ?: throw AccountNotFoundException("Can not find transactions with accountId $accountId.")
        val newBalance = actualBalance.plus(amount)
        val newTransaction = Transaction(accountId, amount, newBalance, now(clock))

        return transactionRepository.add(newTransaction)
            ?: throw UnableToAddIntoDatabaseException("Unable to add $newTransaction into database.")
    }

    fun withdraw(accountId: String, amount: BigDecimal): Transaction? {
        checkWithdrawalAmount(amount)
        val actualBalance = transactionRepository.getBalance(accountId)
            ?: throw AccountNotFoundException("Can not find transactions with accountId $accountId.")
        if (!isEnoughFunds(amount, actualBalance)) throw NotEnoughFundsException("Not enough funds.")
        val newBalance = actualBalance.plus(amount)
        val newTransaction = Transaction(accountId, amount, newBalance, now(clock))

        return transactionRepository.add(newTransaction)
            ?: throw UnableToAddIntoDatabaseException("Unable to add $newTransaction into database.")
    }

    private fun isEnoughFunds(amount: BigDecimal, balance: BigDecimal) = amount.abs() <= balance

    fun displayOperations(accountId: String, printFunc: (List<Transaction>) -> Unit) =
        transactionRepository.findByAccountId(accountId)
            ?.let {
                printFunc.invoke(it)
            }
            ?: throw AccountNotFoundException("Can not find transactions with accountId $accountId.")
}
