package com.example.todoya.domain.model

data class RepositoryException(val code: Int, override val message: String): Exception(message)
