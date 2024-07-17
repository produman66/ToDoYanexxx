package network.error

data class RepositoryException(val code: Int, override val message: String): Exception(message)
