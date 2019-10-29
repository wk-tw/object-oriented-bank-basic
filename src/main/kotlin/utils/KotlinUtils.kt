package utils

/**
 * Allow null case operation before not null treatment
 */
fun <T> T?.switchIfNull(func: () -> Nothing): T {
    return this?.let { this }
        ?: func()
}