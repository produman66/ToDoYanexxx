package com.example.todoya.presentation.ui.editTodoScreen

import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.repository.ConnectivityObserver

data class EditTodoUiState(
    val selectedTodoItem: TodoItem? = null,
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val errorCode: Int? = null
)