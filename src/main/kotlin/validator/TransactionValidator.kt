package validator

import java.math.BigDecimal

fun checkWithdrawalTransaction(amount: BigDecimal) {
    check(amount < BigDecimal.ZERO) { "Amount $amount must be negative." }
}

fun checkDepositTransaction(amount: BigDecimal) {
    check(amount > BigDecimal.ZERO) { "Amount $amount must not be negative." }
}