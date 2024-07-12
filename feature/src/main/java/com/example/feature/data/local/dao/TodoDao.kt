package com.example.feature.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.feature.data.local.model.TodoItem
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for managing TodoItem entities in the database.
 */
@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items WHERE isDeleted = 0")
    fun getAllTodo(): Flow<List<TodoItem>>


    @Query("SELECT * FROM todo_items WHERE id = :id")
    suspend fun getTodoById(id: String): TodoItem?


    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteTodoById(id: String)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoItem)


    @Query("UPDATE todo_items SET isCompleted = NOT isCompleted, isModified = 0 WHERE id = :id")
    suspend fun toggleCompleted(id: String)


    @Query("SELECT COUNT(*) FROM todo_items WHERE isCompleted = 1 AND isDeleted = 0")
    fun getCompletedTodoCount(): Flow<Int>


    @Query("SELECT * FROM todo_items WHERE isDeleted = 0  AND isModified = 0")
    fun getUnsyncedTodos(): List<TodoItem>


    @Query("SELECT * FROM todo_items WHERE isDeleted = 1")
    fun getDeletedTodos(): List<TodoItem>


    @Query("UPDATE todo_items SET isSynced = 1 WHERE id = :id")
    suspend fun markTodoAsSynced(id: String)


    @Query("UPDATE todo_items SET isDeleted = 1 WHERE id = :id")
    suspend fun markTodoAsDeleted(id: String)


    @Query("UPDATE todo_items SET isModified = 1 WHERE id = :id")
    suspend fun markTodoAsModified(id: String)


    @Query("DELETE FROM todo_items")
    suspend fun deleteAllTodos()

}