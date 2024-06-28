package com.example.todoya.data.repository

import android.util.Log
import com.example.todoya.data.dao.TodoDao
import com.example.todoya.data.entity.TodoItem
import com.example.todoya.domain.repository.ITodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TodoItemsRepository(private val todoDao: TodoDao) : ITodoItemsRepository {

    override val allTodo: Flow<List<TodoItem>> = todoDao.getAllTodo()

    override val incompleteTodo: Flow<List<TodoItem>> = allTodo.map { todoList ->
        todoList.filter { !it.isCompleted }
    }

    override suspend fun insert(todo: TodoItem) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.insert(todo)
            } catch (e: Exception) {
                Log.d("TodoItemsRepository", "Error inserting todo item: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun deleteTodoById(id: String) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.deleteTodoById(id)
            } catch (e: Exception) {
                Log.d("TodoItemsRepository", "Error deleting todo item with id $id: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun toggleCompletedById(id: String) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.toggleCompleted(id)
            } catch (e: Exception) {
                Log.d("TodoItemsRepository", "Error toggling completed status for todo item with id $id: ${e.message}")
                throw e
            }
        }
    }

    override fun getTodoById(id: String): Flow<TodoItem?> = todoDao.getTodoById(id)

    override fun getCompletedTodoCount(): Flow<Int> = todoDao.getCompletedTodoCount()
}