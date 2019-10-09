package validator

import model.Transaction
import model.TransactionStatus
import model.TransactionType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Stream

internal class TransactionValidatorKtTest {
    companion object {
        @JvmStatic
        fun createValidWithdrawalTransactions() = Stream.of(
            Arguments.of(
                Transaction(
                    accountId = "FR3217569000403186528461V35",
                    money = BigDecimal(-500),
                    balance = null,
                    transactionType = TransactionType.WITHDRAWAL,
                    transactionStatus = TransactionStatus.PENDING,
                    creationDate = LocalDate.now(),
                    requestedExecutionDate = LocalDate.now()
                )
            ),
            Arguments.of(
                Transaction(
                    accountId = "FR3217569000403186528461V35",
                    money = BigDecimal(-200000),
                    balance = null,
                    transactionType = TransactionType.WITHDRAWAL,
                    transactionStatus = TransactionStatus.PENDING,
                    creationDate = LocalDate.now(),
                    requestedExecutionDate = LocalDate.now()
                )
            )
        )

        @JvmStatic
        fun createInvalidWithdrawalTransactions() = Stream.of(
            Arguments.of(
                Transaction(
                    accountId = "FR3217569000403186528461V35",
                    money = BigDecimal(-500),
                    balance = null,
                    transactionType = TransactionType.DEPOSIT,
                    transactionStatus = TransactionStatus.PENDING,
                    creationDate = LocalDate.now(),
                    requestedExecutionDate = LocalDate.now()
                )
            ),
            Arguments.of(
                Transaction(
                    accountId = "FR3217569000403186528461V35",
                    money = BigDecimal(200000),
                    balance = null,
                    transactionType = TransactionType.WITHDRAWAL,
                    transactionStatus = TransactionStatus.PENDING,
                    creationDate = LocalDate.now(),
                    requestedExecutionDate = LocalDate.now()
                )
            )
        )

        @JvmStatic
        fun createValidDepositTransactions() = Stream.of(
            Arguments.of(
                Transaction(
                    accountId = "FR3217569000403186528461V35",
                    money = BigDecimal(50),
                    balance = null,
                    transactionType = TransactionType.DEPOSIT,
                    transactionStatus = TransactionStatus.PENDING,
                    creationDate = LocalDate.now(),
                    requestedExecutionDate = LocalDate.now()
                )
            ),
            Arguments.of(
                Transaction(
                    accountId = "FR3217569000403186528461V35",
                    money = BigDecimal(200000),
                    balance = null,
                    transactionType = TransactionType.DEPOSIT,
                    transactionStatus = TransactionStatus.PENDING,
                    creationDate = LocalDate.now(),
                    requestedExecutionDate = LocalDate.now()
                )
            )
        )

        @JvmStatic
        fun createInvalidDepositTransactions() = Stream.of(
            Arguments.of(
                Transaction(
                    accountId = "FR3217569000403186528461V35",
                    money = BigDecimal(50),
                    balance = null,
                    transactionType = TransactionType.WITHDRAWAL,
                    transactionStatus = TransactionStatus.PENDING,
                    creationDate = LocalDate.now(),
                    requestedExecutionDate = LocalDate.now()
                )
            ),
            Arguments.of(
                Transaction(
                    accountId = "FR3217569000403186528461V35",
                    money = BigDecimal(-200000),
                    balance = null,
                    transactionType = TransactionType.DEPOSIT,
                    transactionStatus = TransactionStatus.PENDING,
                    creationDate = LocalDate.now(),
                    requestedExecutionDate = LocalDate.now()
                )
            )
        )
    }

    @ParameterizedTest
    @MethodSource("createValidWithdrawalTransactions")
    fun `checkWithdrawalTransaction, should succeed`(transaction: Transaction) {
        assertDoesNotThrow { checkWithdrawalTransaction(transaction) }
    }

    @ParameterizedTest
    @MethodSource("createInvalidWithdrawalTransactions")
    fun `checkWithdrawalTransaction, invalid data,should throw`(transaction: Transaction) {
        Assertions.assertThatThrownBy { checkWithdrawalTransaction(transaction) }
            .isExactlyInstanceOf(IllegalStateException::class.java)
    }

    @ParameterizedTest
    @MethodSource("createValidDepositTransactions")
    fun `checkDepositTransaction, should succeed`(transaction: Transaction) {
        assertDoesNotThrow { checkDepositTransaction(transaction) }
    }

    @ParameterizedTest
    @MethodSource("createInvalidDepositTransactions")
    fun `checkDepositTransaction, invalid data,should throw`(transaction: Transaction) {
        Assertions.assertThatThrownBy { checkDepositTransaction(transaction) }
            .isExactlyInstanceOf(IllegalStateException::class.java)
    }
}