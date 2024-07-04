package com.example.todoya.presentation.ui

data class RepositoryException(val code: Int, override val message: String): Exception(message)
