package com.example.todoya

import android.app.Application
import com.example.todoya.data.room.TodoRoomDatabase
import com.example.todoya.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TodoApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { TodoRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy {TodoItemsRepository(database.todoDao()) }
}