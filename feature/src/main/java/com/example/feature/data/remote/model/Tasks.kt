package com.example.feature.data.remote.model


/**
 * Represents a collection of Task objects.
 */
data class Tasks(
    val list: List<Task>,
    val revision: Int,
    val status: String
)