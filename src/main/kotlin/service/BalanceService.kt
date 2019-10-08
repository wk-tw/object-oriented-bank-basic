package service

import client.TransactionClient
import model.TransactionStatus
import java.math.BigDecimal

class BalanceService(private val transactionClient: TransactionClient) {
    fun getBalance(accountId: String): BigDecimal =
        transactionClient.findById(accountId)
            .takeIf { it.isNotEmpty() }
            ?.filter { it.transactionStatus == TransactionStatus.SUCCESS }
            ?.maxBy { it.requestedExecutionDate }
            ?.balance
            ?: throw IllegalStateException("Unable to retrieve balance")
}
