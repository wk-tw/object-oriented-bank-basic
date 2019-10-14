package utils

import exception.PaddingException
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Any.pad(maxLength: Int): String =
    when (this) {
        is String -> this
        is Instant -> toFormattedString()
        is BigDecimal -> toPlainString()
        else -> toString()
    }
        .takeIf {
            it.length <= maxLength
        }
        ?.let {
            it.padEnd((maxLength + it.length) / 2)
                .padStart(maxLength)
        }
        ?: throw PaddingException("Input $this is bigger than expected length $maxLength")


private fun Instant.toFormattedString(): String = DateTimeFormatter
    .ofPattern("YYYY-MM-dd HH'H'mm")
    .withZone(ZoneOffset.UTC)
    .format(this)
