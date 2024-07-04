package com.example.todoya.domain.model


data class Task(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Int,
    val done: Boolean,
    val color: String,
    val created_at: Int,
    val changed_at: Int,
    val last_updated_by: String,
)