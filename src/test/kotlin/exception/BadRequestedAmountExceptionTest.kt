package exception

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class BadRequestedAmountExceptionTest {

    @ParameterizedTest
    @ValueSource(
        strings = [
            "custom message 1",
            "custom message 2",
            "custom message 3",
            "custom message 4"
        ]
    )
    fun `getMessage, should succeed`(message: String) {
        Assertions.assertThatThrownBy { throw BadRequestedAmountException(message) }
            .isExactlyInstanceOf(BadRequestedAmountException::class.java)
            .hasMessage(message)
    }
}