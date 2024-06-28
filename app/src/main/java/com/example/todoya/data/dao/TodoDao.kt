package com.example.todoya.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todoya.data.entity.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items")
    fun getAllTodo(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE id = :id")
    fun getTodoById(id: String): Flow<TodoItem?>

    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteTodoById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoItem)

    @Query("UPDATE todo_items SET isCompleted = NOT isCompleted WHERE id = :id")
    suspend fun toggleCompleted(id: String)

    @Query("SELECT COUNT(*) FROM todo_items WHERE isCompleted = 1")
    fun getCompletedTodoCount(): Flow<Int>

}