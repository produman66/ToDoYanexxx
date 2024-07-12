package com.example.todoya.presentation.editTodoScreen

import com.example.core.network_manager.ConnectivityObserver
import com.example.feature.data.local.model.TodoItem

data class EditTodoUiState(
    val selectedTodoItem: TodoItem? = null,
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val errorCode: Int? = null
)