package utils

import exception.PaddingException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant

internal class FormatterUtilsKtTest {

    @ParameterizedTest
    @CsvSource(
        "Toto, 11",
        "New Input String, 20"
    )
    fun `pad, String, should succeed`(input: String, expectedLength: Int) {
        assertThat(input.pad(expectedLength).length).isEqualTo(expectedLength)
    }

    @ParameterizedTest
    @CsvSource(
        "2019-01-18T12:11:56Z, 18",
        "2019-05-29T23:51:56Z, 20"
    )
    fun `pad, Instant, should succeed`(input: Instant, expectedLength: Int) {
        assertThat(input.pad(expectedLength).length).isEqualTo(expectedLength)
    }

    @ParameterizedTest
    @CsvSource(
        "300.00, 10",
        "-500.20, 9"
    )
    fun `pad, BigDecimal, should succeed`(input: Double, expectedLength: Int) {
        val bigDecimal = BigDecimal(input).setScale(2, RoundingMode.FLOOR)
        assertThat(bigDecimal.pad(expectedLength).length).isEqualTo(expectedLength)
    }

    @ParameterizedTest
    @CsvSource(
        "very big input, 5"
    )
    fun `pad, input bigger than max length, should throw`(input: String, expectedLength: Int) {
        Assertions.assertThatThrownBy { input.pad(expectedLength) }
            .isExactlyInstanceOf(PaddingException::class.java)
    }
}