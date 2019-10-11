package repository

import model.Transaction
import java.math.BigDecimal

interface TransactionRepository {
    fun findByAccountId(id: String): List<Transaction>?
    fun getBalance(id: String): BigDecimal?
    fun add(transaction: Transaction): Transaction?
}