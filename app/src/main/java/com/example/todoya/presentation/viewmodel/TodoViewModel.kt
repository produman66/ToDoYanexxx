package com.example.todoya.presentation.viewmodel


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoya.data.repository.TodoItemsRepositoryImpl
import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.repository.TodoItemsRepository
import com.example.todoya.presentation.ui.ConnectivityObserver
import com.example.todoya.presentation.ui.NetworkConnectivityObserver
import com.example.todoya.presentation.ui.RepositoryException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoItemsRepository) : ViewModel() {


    private lateinit var connectivityObserver: ConnectivityObserver
    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val networkStatus: StateFlow<ConnectivityObserver.Status> get() = _networkStatus

    fun initializeConnectivityObserver(context: Context) {
        if (!::connectivityObserver.isInitialized) {
            connectivityObserver = NetworkConnectivityObserver(context)
            viewModelScope.launch {
                    connectivityObserver.observe().collect { status ->
                    _networkStatus.value = status
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            while (true) {
                delay(60000)
                syncWithServer()
                Log.d("ViewModel", "Обновлено")
            }
        }
    }

    var isEyeClosed = MutableLiveData(false)

    val allTodo: LiveData<List<TodoItem>> = repository.allTodo.asLiveData()
    val todoIncomplete: LiveData<List<TodoItem>> = repository.incompleteTodo.asLiveData()

    private val _selectedTodoItem = MutableStateFlow<TodoItem?>(null)
    val selectedTodoItem: StateFlow<TodoItem?> get() = _selectedTodoItem

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _errorCode = MutableLiveData<Int>()
    val errorCode: LiveData<Int> get() = _errorCode



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

class TodoViewModelFactory(private val repository: TodoItemsRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}