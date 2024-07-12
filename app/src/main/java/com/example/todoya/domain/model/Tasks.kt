package com.example.todoya.domain.model


/**
 * Represents a collection of Task objects.
 */
data class Tasks(
    val list: List<Task>,
    val revision: Int,
    val status: String
)