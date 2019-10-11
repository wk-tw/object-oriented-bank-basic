package utils

import model.Transaction
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
        requestedExecutionDate = now().minusDays(10)
    ),
    Transaction(
        accountId = "FR3217569000403186528461V35",
        money = BigDecimal(-3000),
        balance = BigDecimal(2000),
        requestedExecutionDate = now().minusDays(9)
    ),
    Transaction(
        accountId = "FR3217569000403186528461V35",
        money = BigDecimal(-500),
        balance = BigDecimal(1500),
        requestedExecutionDate = now().minusDays(8)
    ),
    Transaction(
        accountId = "FR3217569000403186528461V35",
        money = BigDecimal(-1000),
        balance = BigDecimal(500),
        requestedExecutionDate = now().minusDays(3)
    )
)

private fun createFR6017569000704817168116U94() = listOf<Transaction>(
    Transaction(
        accountId = "FR6017569000704817168116U94",
        money = BigDecimal(2000),
        balance = BigDecimal(2000),
        requestedExecutionDate = now().minusDays(10)
    ),
    Transaction(
        accountId = "FR6017569000704817168116U94",
        money = BigDecimal(-1000),
        balance = BigDecimal(1000),
        requestedExecutionDate = now().minusDays(8)
    ),
    Transaction(
        accountId = "FR6017569000704817168116U94",
        money = BigDecimal(-1000),
        balance = BigDecimal(0),
        requestedExecutionDate = now().minusDays(6)
    ),
    Transaction(
        accountId = "FR6017569000704817168116U94",
        money = BigDecimal(-1000),
        balance = BigDecimal(0),
        requestedExecutionDate = now()
    )
)

private fun createFR4930003000302945844589B40() = emptyList<Transaction>()