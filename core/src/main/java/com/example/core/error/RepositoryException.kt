package com.example.core.error

data class RepositoryException(val code: Int, override val message: String): Exception(message)
