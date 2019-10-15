package exception

class AccountNotFoundException(override val message: String) : Exception(message)