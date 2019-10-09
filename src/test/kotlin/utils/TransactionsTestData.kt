package utils

import model.Transaction
import model.TransactionStatus.*
import model.TransactionType.DEPOSIT
import model.TransactionType.WITHDRAWAL
import java.math.BigDecimal
import java.time.LocalDate.now

fun getTransactionsById(accountId: String): List<Transaction> =
    when (accountId) {
        "FR3217569000403186528461V35" -> createFR3217569000403186528461V35()
        "FR6017569000704817168116U94" -> createFR6017569000704817168116U94()
        "FR4930003000302945844589B40" -> createFR4930003000302945844589B40()
        else -> throw IllegalStateException("Unable to find transactions with id $accountId")
    }

private fun createFR3217569000403186528461V35() = listOf<Transaction>(
    Transaction(
        accountId = "FR3217569000403186528461V35",
        money = BigDecimal(2000),
        balance = BigDecimal(2000),
        transactionType = DEPOSIT,
        transactionStatus = SUCCESS,
        creationDate = now().minusDays(10),
        requestedExecutionDate = now().minusDays(10)
    ),
    Transaction(
        accountId = "FR3217569000403186528461V35",
        money = BigDecimal(-3000),
        balance = BigDecimal(2000),
        transactionType = WITHDRAWAL,
        transactionStatus = REJECTED,
        creationDate = now().minusDays(9),
        requestedExecutionDate = now().minusDays(9)
    ),
    Transaction(
        accountId = "FR3217569000403186528461V35",
        money = BigDecimal(-500),
        balance = BigDecimal(1500),
        transactionType = WITHDRAWAL,
        transactionStatus = SUCCESS,
        creationDate = now().minusDays(8),
        requestedExecutionDate = now().minusDays(8)
    ),
    Transaction(
        accountId = "FR3217569000403186528461V35",
        money = BigDecimal(-1000),
        balance = BigDecimal(500),
        transactionType = WITHDRAWAL,
        transactionStatus = SUCCESS,
        creationDate = now().minusDays(3),
        requestedExecutionDate = now().minusDays(3)
    )
)

fun createDepositTransaction() = Transaction(
    accountId = "FR3217569000403186528461V35",
    money = BigDecimal(1000),
    balance = null,
    transactionType = DEPOSIT,
    transactionStatus = PENDING,
    creationDate = now(),
    requestedExecutionDate = now()
)

fun createAfterDepositTransaction() = Transaction(
    accountId = "FR3217569000403186528461V35",
    money = BigDecimal(1000),
    balance = BigDecimal(1500),
    transactionType = DEPOSIT,
    transactionStatus = SUCCESS,
    creationDate = now(),
    requestedExecutionDate = now()
)

fun createWithdrawalTransaction() = Transaction(
    accountId = "FR3217569000403186528461V35",
    money = BigDecimal(-500),
    balance = null,
    transactionType = WITHDRAWAL,
    transactionStatus = PENDING,
    creationDate = now(),
    requestedExecutionDate = now()
)

fun createAfterWithdrawalTransaction() = Transaction(
    accountId = "FR3217569000403186528461V35",
    money = BigDecimal(-500),
    balance = BigDecimal(0),
    transactionType = WITHDRAWAL,
    transactionStatus = SUCCESS,
    creationDate = now(),
    requestedExecutionDate = now()
)

private fun createFR6017569000704817168116U94() = listOf<Transaction>(
    Transaction(
        accountId = "FR6017569000704817168116U94",
        money = BigDecimal(2000),
        balance = BigDecimal(2000),
        transactionType = DEPOSIT,
        transactionStatus = SUCCESS,
        creationDate = now().minusDays(10),
        requestedExecutionDate = now().minusDays(10)
    ),
    Transaction(
        accountId = "FR6017569000704817168116U94",
        money = BigDecimal(-1000),
        balance = BigDecimal(1000),
        transactionType = WITHDRAWAL,
        transactionStatus = SUCCESS,
        creationDate = now().minusDays(8),
        requestedExecutionDate = now().minusDays(8)
    ),
    Transaction(
        accountId = "FR6017569000704817168116U94",
        money = BigDecimal(-1000),
        balance = BigDecimal(0),
        transactionType = WITHDRAWAL,
        transactionStatus = SUCCESS,
        creationDate = now().minusDays(6),
        requestedExecutionDate = now().minusDays(6)
    ),
    Transaction(
        accountId = "FR6017569000704817168116U94",
        money = BigDecimal(-1000),
        balance = BigDecimal(0),
        transactionType = WITHDRAWAL,
        transactionStatus = REJECTED,
        creationDate = now(),
        requestedExecutionDate = now()
    )
)

private fun createFR4930003000302945844589B40() = emptyList<Transaction>()