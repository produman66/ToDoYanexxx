package com.example.todoya.feature.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.data.local.model.TodoItem
import com.example.feature.data.repository.TodoItemsRepositoryImpl
import com.example.todoya.presentation.editTodoScreen.EditTodoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditTodoViewModel @Inject constructor (
    private val repository: TodoItemsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTodoUiState())
    val uiState: StateFlow<EditTodoUiState> get() = _uiState

    fun getTodoById(id: String) {
        viewModelScope.launch {
            try {
                val todoItem = repository.getTodoById(id)
                _uiState.update { it.copy(selectedTodoItem = todoItem) }
            } catch (e: com.example.core.error.RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun deleteTodoById(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteTodoById(id)
            } catch (e: com.example.core.error.RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun insert(todo: TodoItem) {
        viewModelScope.launch {
            try {
                repository.insert(todo)
            } catch (e: com.example.core.error.RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorCode = null) }
    }

    fun syncWithServer() {
        viewModelScope.launch {
            try {
                repository.syncWithServer()
            } catch (e: com.example.core.error.RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }
}