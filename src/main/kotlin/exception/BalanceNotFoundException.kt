package exception

class BalanceNotFoundException(override val message: String?) : Exception(message)