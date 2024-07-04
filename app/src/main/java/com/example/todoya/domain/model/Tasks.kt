package com.example.todoya.domain.model

data class Tasks(
    val list: List<Task>,
    val revision: Int,
    val status: String
)