package com.example.todoya.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoya.data.room.TodoItem
import com.example.todoya.repository.TodoItemsRepository
import kotlinx.coroutines.launch

class TodoViewModel (private val repository: TodoItemsRepository) : ViewModel() {

    var isEyeClosed = false

    val allTodo: LiveData<List<TodoItem>> = repository.allTodo
    val todoIncomplete: LiveData<List<TodoItem>> = repository.incompleteTodo

    fun insert(todo: TodoItem) = viewModelScope.launch {
        repository.insert(todo)
    }

    fun getTodoById(id: String): LiveData<TodoItem?> {
        return repository.getTodoById(id)
    }

    fun getCompletedTodoCount(): LiveData<Int> {
        return repository.getCompletedTodoCount()
    }


    fun deleteTodoById(id:String) = viewModelScope.launch {
        repository.deleteTodoById(id)
    }

    fun toggleCompletedById(id:String) = viewModelScope.launch {
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