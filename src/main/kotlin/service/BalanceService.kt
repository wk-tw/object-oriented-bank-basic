package service

import client.TransactionClient
import exception.BalanceNotFoundException
import java.math.BigDecimal

class BalanceService(private val transactionClient: TransactionClient) {
    fun getBalance(accountId: String): BigDecimal =
        transactionClient.findById(accountId)
            .takeIf { it.isNotEmpty() }
            ?.maxBy { it.requestedExecutionDate }
            ?.balance
            ?: throw BalanceNotFoundException("Unable to retrieve balance")
}
