package com.example.todoya.presentation.homeScreen


import com.example.core.network_manager.ConnectivityObserver
import com.example.feature.data.local.model.TodoItem


data class HomeScreenUiState(
    val curItemsList: List<TodoItem> = emptyList(),
    val countCompletedTodo: Int = 0,
    val isEyeClosed: Boolean = false,
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val errorCode: Int? = null
)