package validator

import exception.BadRequestedAmountException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

internal class TransactionValidatorKtTest {

    @ParameterizedTest
    @ValueSource(
        doubles = [
            -20.50,
            -400.00,
            -10000.00
        ]
    )
    fun `checkWithdrawalTransaction, should succeed`(double: Double) {
        val amount = BigDecimal.valueOf(double)
        assertDoesNotThrow { checkWithdrawalTransaction(amount) }
    }

    @ParameterizedTest
    @ValueSource(
        doubles = [
            20.50,
            400.00,
            10000.00
        ]
    )
    fun `checkWithdrawalTransaction, invalid data,should throw`(double: Double) {
        val amount = BigDecimal.valueOf(double)
        Assertions.assertThatThrownBy { checkWithdrawalTransaction(amount) }
            .isExactlyInstanceOf(BadRequestedAmountException::class.java)
    }

    @ParameterizedTest
    @ValueSource(
        doubles = [
            20.50,
            400.00,
            10000.00
        ]
    )
    fun `checkDepositTransaction, should succeed`(double: Double) {
        val amount = BigDecimal.valueOf(double)
        assertDoesNotThrow { checkDepositTransaction(amount) }
    }

    @ParameterizedTest
    @ValueSource(
        doubles = [
            -20.50,
            -400.00,
            -10000.00
        ]
    )
    fun `checkDepositTransaction, invalid data,should throw`(double: Double) {
        val amount = BigDecimal.valueOf(double)
        Assertions.assertThatThrownBy { checkDepositTransaction(amount) }
            .isExactlyInstanceOf(BadRequestedAmountException::class.java)
    }
}