package validator

import exception.BadRequestedAmountException
import java.math.BigDecimal

fun checkWithdrawalTransaction(amount: BigDecimal) {
    if (amount >= BigDecimal.ZERO) throw BadRequestedAmountException("Amount $amount must be strictly negative.")
}

fun checkDepositTransaction(amount: BigDecimal) {
    if (amount <= BigDecimal.ZERO) throw BadRequestedAmountException("Amount $amount must be strictly positive.")
}