package presentation.homeScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.local.model.TodoItem
import data.repository.TodoItemsRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.error.RepositoryException
import network.observer.NetworkConnectivityObserver
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val repository: TodoItemsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadTasks()
        loadUndoTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.allTodo.collect { tasks ->
                updateFilteredTasks(tasks)
            }
        }
    }

    private fun loadUndoTasks() {
        viewModelScope.launch {
            repository.undoTodo.collect { tasks ->
                undoTasks(tasks)
            }
        }
    }

    private fun undoTasks(allTasks: List<TodoItem>) {
        Log.d("uuuuuuuuuu", "undoTasks: ${allTasks}")
        val item = if (allTasks.isNotEmpty()) allTasks.first() else null
        Log.d("uuuuuuuuuu", "undoTasks: ${item}")
        _uiState.update { it.copy(
            undoList = item
        ) }
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
        val connectivityObserver =
            NetworkConnectivityObserver(context)
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


    fun deleteTodoById(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteTodoById(id)
            } catch (e: RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }

    fun undoTodoById(id: String) {
        viewModelScope.launch {
            try {
                repository.undoTodoById(id)
            } catch (e: RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }
}