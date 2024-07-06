package com.example.todoya.presentation.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.repository.TodoItemsRepository
import com.example.todoya.domain.repository.ConnectivityObserver
import com.example.todoya.domain.manager.NetworkConnectivityObserver
import com.example.todoya.domain.model.RepositoryException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling business logic related to Todo items.
 */
class TodoViewModel(private val repository: TodoItemsRepository) : ViewModel() {

    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val networkStatus: StateFlow<ConnectivityObserver.Status> get() = _networkStatus

    private val _selectedTodoItem = MutableStateFlow<TodoItem?>(null)
    val selectedTodoItem: StateFlow<TodoItem?> get() = _selectedTodoItem

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _errorCode = MutableStateFlow<Int?>(null)
    val errorCode: StateFlow<Int?> get() = _errorCode

    var isEyeClosed = MutableStateFlow(false)

    val allTodo: Flow<List<TodoItem>>
        get() = repository.allTodo

    val todoIncomplete: Flow<List<TodoItem>>
        get() = repository.incompleteTodo

    fun clearError() {
        _errorCode.value = null
    }


    fun initializeConnectivityObserver(context: Context) {
        val connectivityObserver = NetworkConnectivityObserver(context)
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _networkStatus.value = status
            }
        }
    }


    fun insert(todo: TodoItem) {
        viewModelScope.launch {
            try {
                repository.insert(todo)
            } catch (e: RepositoryException) {
                _error.value = e.message
                _errorCode.value = e.code
            }
        }
    }


    fun getTodoById(id: String) {
        viewModelScope.launch {
            try {
                _selectedTodoItem.value = repository.getTodoById(id)
            } catch (e: RepositoryException) {
                _error.value = e.message
                _errorCode.value = e.code
            }
        }
    }


    fun getCompletedTodoCount(): LiveData<Int> {
        return repository.getCompletedTodoCount().asLiveData()
    }


    fun deleteTodoById(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteTodoById(id)
            } catch (e: RepositoryException) {
                _error.value = e.message
                _errorCode.value = e.code
            }
        }
    }


    fun toggleCompletedById(id: String) {
        viewModelScope.launch {
            try {
                repository.toggleCompletedById(id)
            } catch (e: RepositoryException) {
                _error.value = e.message
                _errorCode.value = e.code
            }
        }
    }


    fun syncWithServer() {
        viewModelScope.launch {
            try {
                repository.syncWithServer()
            } catch (e: RepositoryException) {
                _error.value = e.message
                _errorCode.value = e.code
            }
        }
    }
}