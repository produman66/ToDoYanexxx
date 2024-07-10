package com.example.todoya.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoya.data.NetworkConnectivityObserver
import com.example.todoya.data.repository.TodoItemsRepositoryImpl
import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.model.RepositoryException
import com.example.todoya.presentation.ui.homeScreen.HomeScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val repository: TodoItemsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.allTodo.collect { tasks ->
                updateFilteredTasks(tasks)
            }
        }
    }

    private fun updateFilteredTasks(allTasks: List<TodoItem>) {
        val filteredTasks = if (_uiState.value.isEyeClosed) {
            allTasks
        } else {
            allTasks.filter { !it.isCompleted }
        }
        _uiState.update { it.copy(
            curItemsList = filteredTasks,
            countCompletedTodo = allTasks.count { it.isCompleted }
        ) }
    }

    fun initializeConnectivityObserver(context: Context) {
        val connectivityObserver = NetworkConnectivityObserver(context)
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _uiState.update { it.copy(networkStatus = status) }
            }
        }
    }

    fun toggleCompletedById(id: String) {
        viewModelScope.launch {
            try {
                repository.toggleCompletedById(id)
                loadTasks()
            } catch (e: RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun syncWithServer() {
        viewModelScope.launch {
            try {
                repository.syncWithServer()
                loadTasks()
            } catch (e: RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun toggleEyeClosed() {
        val newIsEyeClosed = !_uiState.value.isEyeClosed
        _uiState.update { it.copy(isEyeClosed = newIsEyeClosed) }
        viewModelScope.launch {
            val allTasks = repository.allTodo.first()
            updateFilteredTasks(allTasks)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorCode = null) }
    }
}