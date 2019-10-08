package client

import model.Transaction

interface TransactionClient {
    fun findById(id: String): List<Transaction>
    fun save(transaction: Transaction): Transaction
}