package com.example.todoya.domain.repository

import com.example.todoya.data.room.entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {
    val allTodo: Flow<List<TodoItem>>
    val incompleteTodo: Flow<List<TodoItem>>

    suspend fun insert(todo: TodoItem)
    suspend fun deleteTodoById(id: String)
    suspend fun toggleCompletedById(id: String)
    suspend fun getTodoById(id: String): TodoItem?
    fun getCompletedTodoCount(): Flow<Int>

    suspend fun syncWithServer()
    suspend fun getServerRevision():Int
}