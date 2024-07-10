package com.example.todoya.data.repository

import com.example.todoya.data.retrofit.TodoApiService
import com.example.todoya.data.room.dao.TodoDao
import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.model.Request
import com.example.todoya.data.TaskConverter
import com.example.todoya.domain.repository.TodoItemsRepository
import com.example.todoya.domain.model.RepositoryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException


/**
 * Repository responsible for managing todo items data from local database and remote server.
 */

class TodoItemsRepositoryImpl(
    private val todoDao: TodoDao,
    private val todoApiService: TodoApiService,
    private var token: String
) : TodoItemsRepository {

    override fun updateAuthToken(token: String) {
        this.token = token
    }


    override val allTodo: Flow<List<TodoItem>>
        get() = todoDao.getAllTodo()


    override val incompleteTodo: Flow<List<TodoItem>>
        get() = allTodo.map { todoList -> todoList.filter { !it.isCompleted } }


    override suspend fun getTodoById(id: String): TodoItem? = todoDao.getTodoById(id)


    override fun getCompletedTodoCount(): Flow<Int> = todoDao.getCompletedTodoCount()


    override suspend fun insert(todo: TodoItem) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.insert(todo)
            } catch (e: Exception) {
                throw RepositoryException(1, "Error inserting todo item: ${e.message}")
            }
        }
    }


    override suspend fun deleteTodoById(id: String) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.markTodoAsDeleted(id)
            } catch (e: Exception) {
                throw RepositoryException(2, "Error deleting todo item with id $id: ${e.message}")
            }
        }
    }


    override suspend fun toggleCompletedById(id: String) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.toggleCompleted(id)
            } catch (e: Exception) {
                throw RepositoryException(3, "Error toggling completed status for todo item with id $id: ${e.message}")
            }
        }
    }

    override suspend fun syncWithServer() {
        withContext(Dispatchers.IO) {
            try {
                syncUnsyncedTodos()
                syncDeletedTodos()
                syncAllTodos()
            } catch (e: IOException) {
                throw RepositoryException(6, "Network error while syncing")
            } catch (e: Exception) {
                throw RepositoryException(7, "Error syncing: ${e.message}")
            }
        }
    }

    private suspend fun syncUnsyncedTodos() {
        val unsyncedTodos = todoDao.getUnsyncedTodos()
        unsyncedTodos.forEach { todo ->
            try {
                val revision = getServerRevision()
                val requestBody = Request(TaskConverter.toTask(todo))
                when {
                    todo.isSynced -> {
                        val response = todoApiService.updateTodoItem(todo.id, revision, requestBody, token).execute()
                        if (response.isSuccessful) {
                            todoDao.markTodoAsSynced(todo.id)
                            todoDao.markTodoAsModified(todo.id)
                        } else { throw RepositoryException(response.code(), "Error updating todo item with id ${todo.id}: ${response.errorBody()?.string()}") }
                    }
                    else -> {
                        val response = todoApiService.addTodoItem(revision, requestBody, token).execute()
                        if (response.isSuccessful) {
                            todoDao.markTodoAsSynced(todo.id)
                            todoDao.markTodoAsModified(todo.id)
                        } else { throw RepositoryException(response.code(), "Error adding todo item with id ${todo.id}: ${response.errorBody()?.string()}") }
                    }
                }
            } catch (e: Exception) { throw RepositoryException(4, "Error syncing todo items: ${e.message}")
            } catch (e: IOException) { throw RepositoryException(6, "Network error while syncing")
            }
        }
    }

    private suspend fun syncDeletedTodos() {
        val deletedTodos = todoDao.getDeletedTodos()
        deletedTodos.forEach { todo ->
            try {
                val revision = getServerRevision()
                val response = todoApiService.deleteTodoItem(todo.id,
                    revision,
                    token
                ).execute()
                if (response.isSuccessful) {
                    todoDao.deleteTodoById(todo.id)
                } else {
                    throw RepositoryException(
                        response.code(),
                        "Error deleting todo item with id ${todo.id}: ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            } catch (e: Exception) {
                throw RepositoryException(5, "Error deleting todo items: ${e.message}")
            } catch (e: IOException) {
                throw RepositoryException(6, "Network error while syncing")
            }
        }
    }

    private suspend fun syncAllTodos() {
        try {
            val allTodoResponse = todoApiService.getTodoList(token).execute()
            if (allTodoResponse.isSuccessful) {
                todoDao.deleteAllTodos()
                allTodoResponse.body()?.list?.forEach { todo ->
                    insert(TaskConverter.toTodoItem(todo))
                }
            } else {
                throw RepositoryException(
                    allTodoResponse.code(),
                    "Failed to fetch todo list from server"
                )
            }
        } catch (e: Exception) {
            throw RepositoryException(7, "Error syncing all todos: ${e.message}")
        }
    }


    override suspend fun getServerRevision(): Int {
        return try {
            val response = withContext(Dispatchers.IO) {
                todoApiService.getTodoList(token).execute()
            }
            if (response.isSuccessful) {
                val tasks = response.body()
                tasks?.revision ?: 0
            } else {
                throw RepositoryException(response.code(), "Failed to get server revision")
            }
        } catch (e: IOException) {
            throw RepositoryException(6, "Network error while fetching server revision")
        } catch (e: Exception) {
            throw RepositoryException(7, "Error fetching server revision: ${e.message}")
        }
    }
}