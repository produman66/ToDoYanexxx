package com.example.todoya.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items")
    fun getAllTodo(): LiveData<List<TodoItem>>

    @Query("SELECT * FROM todo_items WHERE id = :id")
    fun getTodoById(id: String): LiveData<TodoItem?>

    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteTodoById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoItem)

    @Query("UPDATE todo_items SET isCompleted = NOT isCompleted WHERE id = :id")
    suspend fun toggleCompleted(id: String)

    @Query("SELECT COUNT(*) FROM todo_items WHERE isCompleted = 1")
    fun getCompletedTodoCount(): LiveData<Int>

}