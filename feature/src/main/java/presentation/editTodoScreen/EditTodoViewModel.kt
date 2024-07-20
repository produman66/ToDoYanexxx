package presentation.editTodoScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.local.model.TodoItem
import data.repository.TodoItemsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import network.error.RepositoryException
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
            } catch (e: RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
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

    fun insert(todo: TodoItem) {
        viewModelScope.launch {
            try {
                repository.insert(todo)
            } catch (e: RepositoryException) {
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
            } catch (e: RepositoryException) {
                _uiState.update { it.copy(errorCode = e.code) }
            }
        }
    }
}