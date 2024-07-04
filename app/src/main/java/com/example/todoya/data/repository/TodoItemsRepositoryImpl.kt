package com.example.todoya.data.repository

import android.util.Log
import com.example.todoya.data.retrofit.TodoApiService
import com.example.todoya.data.room.dao.TodoDao
import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.model.Request
import com.example.todoya.domain.model.TaskConverter
import com.example.todoya.domain.repository.TodoItemsRepository
import com.example.todoya.presentation.ui.RepositoryException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException

class TodoItemsRepositoryImpl(
    private val todoDao: TodoDao,
    private val todoApiService: TodoApiService,
    private val token: String
) : TodoItemsRepository {

    override val allTodo: Flow<List<TodoItem>> = todoDao.getAllTodo()

    override val incompleteTodo: Flow<List<TodoItem>> =
        allTodo.map { todoList -> todoList.filter { !it.isCompleted } }

    override suspend fun getTodoById(id: String): TodoItem? = todoDao.getTodoById(id)

    override fun getCompletedTodoCount(): Flow<Int> = todoDao.getCompletedTodoCount()


    override suspend fun insert(todo: TodoItem) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.insert(todo)
            } catch (e: Exception) {
                Log.d("Repository", "Error inserting todo item: ${e.message}")
                throw RepositoryException(1, "Error inserting todo item: ${e.message}")
            }
        }
    }


    override suspend fun deleteTodoById(id: String) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.markTodoAsDeleted(id)
            } catch (e: Exception) {
                Log.d("Repository", "Error deleting todo item with id $id: ${e.message}")
                throw RepositoryException(2, "Error deleting todo item with id $id: ${e.message}")
            }
        }
    }


    override suspend fun toggleCompletedById(id: String) {
        withContext(Dispatchers.IO) {
            try {
                todoDao.toggleCompleted(id)
            } catch (e: Exception) {
                Log.d(
                    "Repository",
                    "Error toggling completed status for todo item with id $id: ${e.message}"
                )
                throw RepositoryException(
                    3,
                    "Error toggling completed status for todo item with id $id: ${e.message}"
                )
            }
        }
    }


    override suspend fun syncWithServer() {
        withContext(Dispatchers.IO) {
            val allTodoDB = todoDao.getAllTodo().firstOrNull()
            Log.d("Repository", "Все туду из бд: $allTodoDB")
            val unsyncedTodos = todoDao.getUnsyncedTodos()
            unsyncedTodos.forEach { todo ->
                try {
                    val revision = getServerRevision()
                    val requestBody = Request(TaskConverter.toTask(todo))
                    Log.d("Repository", "Request body: $requestBody")

                    when {
                        todo.isSynced -> {
                            val response = todoApiService.updateTodoItem(
                                todo.id,
                                revision,
                                requestBody,
                                token
                            ).execute()
                            if (response.isSuccessful) {
                                todoDao.markTodoAsSynced(todo.id)
                                todoDao.markTodoAsModified(todo.id)
                                Log.d("Repository", "Обновлена на сервере: ${todo.id}")
                            } else {
                                Log.d(
                                    "Repository",
                                    "Не отправлено: ${todo.id}, code: ${response.code()}, error: ${
                                        response.errorBody()?.string()
                                    }"
                                )
                                throw RepositoryException(
                                    response.code(),
                                    "Error updating todo item with id ${todo.id}: ${
                                        response.errorBody()?.string()
                                    }"
                                )
                            }
                        }

                        else -> {
                            val response = todoApiService.addTodoItem(
                                revision,
                                requestBody,
                                token
                            ).execute()
                            if (response.isSuccessful) {
                                todoDao.markTodoAsSynced(todo.id)
                                todoDao.markTodoAsModified(todo.id)
                                Log.d("Repository", "Отправлена на сервер: ${todo.id}")
                            } else {
                                Log.d(
                                    "Repository",
                                    "Не отправлено: ${todo.id}, code: ${response.code()}, error: ${
                                        response.errorBody()?.string()
                                    }"
                                )
                                throw RepositoryException(
                                    response.code(),
                                    "Error adding todo item with id ${todo.id}: ${
                                        response.errorBody()?.string()
                                    }"
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Repository", "Error syncing todo items: ${e.message}")
                    throw RepositoryException(4, "Error syncing todo items: ${e.message}")
                }
            }

            Log.d("Repository", "После пометки $unsyncedTodos")

            val deletedTodos = todoDao.getDeletedTodos()
            Log.d("Repository", "До удаления $deletedTodos")

            deletedTodos.forEach { todo ->
                try {
                    val revision = getServerRevision()
                    val response = todoApiService.deleteTodoItem(
                        todo.id,
                        revision,
                        token
                    ).execute()
                    if (response.isSuccessful) {
                        todoDao.deleteTodoById(todo.id)
                        Log.d("Repository", "Удалено с сервера: ${todo.id}")
                    } else {
                        Log.d(
                            "Repository",
                            "Не удалилось: ${todo.id}, code: ${response.code()}, error: ${
                                response.errorBody()?.string()
                            }"
                        )
                        throw RepositoryException(
                            response.code(),
                            "Error deleting todo item with id ${todo.id}: ${
                                response.errorBody()?.string()
                            }"
                        )
                    }
                } catch (e: Exception) {
//                    if (deletedTodos.isNotEmpty()){
                        Log.e("Repository", "Error syncing todo items: ${e.message}")
                        throw RepositoryException(5, "Error deleting todo items: ${e.message}")
//                    }
                }
            }

            Log.d("Repository", "После удаления $deletedTodos")

            val allTodoResponse = todoApiService.getTodoList(token).execute()
            Log.d("Repository", "На сервере все элементы ${allTodoResponse.body()?.list}")
            if (allTodoResponse.isSuccessful) {
                todoDao.deleteAllTodos()
                Log.d("Repository", "Бд чиста")
                allTodoResponse.body()?.list?.forEach { todo ->
                    insert(TaskConverter.toTodoItem(todo))
                }
                Log.d("Repository", "Вставили все таски")
            } else {
                Log.e("Repository", "Failed to fetch todo list from server")
                throw RepositoryException(
                    allTodoResponse.code(),
                    "Failed to fetch todo list from server"
                )
            }
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
                Log.e("TodoItemsRepository", "Failed to get server revision: ${response.code()}")
                throw RepositoryException(response.code(), "Failed to get server revision")
            }
        } catch (e: IOException) {
            Log.e(
                "TodoItemsRepository",
                "Network error while fetching server revision: ${e.message}"
            )
            throw RepositoryException(6, "Network error while fetching server revision")
        } catch (e: Exception) {
            Log.e("TodoItemsRepository", "Error fetching server revision: ${e.message}")
            throw RepositoryException(7, "Error fetching server revision: ${e.message}")
        }
    }
}