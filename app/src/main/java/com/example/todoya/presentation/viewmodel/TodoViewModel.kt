package com.example.todoya.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoya.data.entity.TodoItem
import com.example.todoya.data.repository.TodoItemsRepository
import com.example.todoya.domain.repository.ITodoItemsRepository
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: ITodoItemsRepository) : ViewModel() {

    var isEyeClosed = MutableLiveData(false)

    val allTodo: LiveData<List<TodoItem>> = repository.allTodo.asLiveData()
    val todoIncomplete: LiveData<List<TodoItem>> = repository.incompleteTodo.asLiveData()

    fun insert(todo: TodoItem) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun getTodoById(id: String): LiveData<TodoItem?> {
        return repository.getTodoById(id).asLiveData()
    }

    fun getCompletedTodoCount(): LiveData<Int> {
        return repository.getCompletedTodoCount().asLiveData()
    }

    fun deleteTodoById(id: String) = viewModelScope.launch {
        repository.deleteTodoById(id)
    }

    fun toggleCompletedById(id: String) = viewModelScope.launch {
        repository.toggleCompletedById(id)
    }
}

class TodoViewModelFactory(private val repository: TodoItemsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}