package com.example.todoya.presentation.ui.homeScreen

import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.repository.ConnectivityObserver


data class HomeScreenUiState(
    val curItemsList: List<TodoItem> = emptyList(),
    val countCompletedTodo: Int = 0,
    val isEyeClosed: Boolean = false,
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val errorCode: Int? = null
)