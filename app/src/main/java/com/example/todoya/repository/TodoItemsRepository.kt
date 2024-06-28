package com.example.todoya.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.todoya.data.room.TodoDao
import com.example.todoya.data.room.TodoItem

class TodoItemsRepository(private val todoDao: TodoDao) {

    val allTodo: LiveData<List<TodoItem>> = todoDao.getAllTodo()
    val incompleteTodo: LiveData<List<TodoItem>> = MediatorLiveData<List<TodoItem>>().apply {
        addSource(allTodo) { todoList ->
            value = todoList.filter { !it.isCompleted }
        }
    }

    @WorkerThread
    suspend fun insert(todo: TodoItem) {
        todoDao.insert(todo)
    }

    suspend fun deleteTodoById(id: String) {
        todoDao.deleteTodoById(id)
    }

    suspend fun toggleCompletedById(id: String) {
        todoDao.toggleCompleted(id)
    }

    fun getTodoById(id: String): LiveData<TodoItem?> {
        return todoDao.getTodoById(id)
    }

    fun getCompletedTodoCount(): LiveData<Int> {
        return todoDao.getCompletedTodoCount()
    }
}