package com.example.todoya.domain.repository

import com.example.todoya.data.entity.TodoItem
import kotlinx.coroutines.flow.Flow

interface ITodoItemsRepository {
    val allTodo: Flow<List<TodoItem>>
    val incompleteTodo: Flow<List<TodoItem>>

    suspend fun insert(todo: TodoItem)
    suspend fun deleteTodoById(id: String)
    suspend fun toggleCompletedById(id: String)
    fun getTodoById(id: String): Flow<TodoItem?>
    fun getCompletedTodoCount(): Flow<Int>
}