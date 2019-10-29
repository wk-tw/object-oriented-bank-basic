package utils

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class KotlinUtilsKtTest {

    @Test
    fun `switchIfNull, null case, should apply lambda`() {
        val any: Any? = null
        val exceptionMessage = "Custom exception"
        assertThatThrownBy { any.switchIfNull { throw IllegalStateException(exceptionMessage) } }
            .isExactlyInstanceOf(IllegalStateException::class.java)
            .hasMessage(exceptionMessage)
    }

    @Test
    fun `switchIfNull, not null string, should return this`() {
        val any: String = "Something there"
        val response = any.switchIfNull { throw IllegalStateException("Custom exception") }

        assertThat(response).isInstanceOf(String::class.java)
        assertThat(response).isEqualTo(any)
    }
}