package com.example.todoya

import android.app.Application
import com.example.todoya.data.room.db.TodoRoomDatabase
import com.example.todoya.data.repository.TodoItemsRepositoryImpl
import com.example.todoya.data.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class TodoApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { TodoRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { TodoItemsRepositoryImpl(database.todoDao(), RetrofitInstance.api, "OAuth y0_AgAAAAA0grqHAARC0QAAAAEJTyUiAAAPF34WQZpHT4ke2su4w9FQaM4ozA") }

}