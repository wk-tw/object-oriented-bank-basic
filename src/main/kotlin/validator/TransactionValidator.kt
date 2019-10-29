package validator

import exception.BadRequestedAmountException
import java.math.BigDecimal

fun checkWithdrawalAmount(amount: BigDecimal) {
    if (amount >= BigDecimal.ZERO) throw BadRequestedAmountException("Amount $amount must be strictly negative.")
}

fun checkDepositAmount(amount: BigDecimal) {
    if (amount <= BigDecimal.ZERO) throw BadRequestedAmountException("Amount $amount must be strictly positive.")
}