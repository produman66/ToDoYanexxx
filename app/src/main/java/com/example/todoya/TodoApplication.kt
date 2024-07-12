package com.example.todoya

import android.app.Application
import com.example.todoya.data.repository.TodoItemsRepositoryImpl
import com.example.todoya.data.retrofit.RetrofitInstance
import com.example.todoya.data.room.db.TodoRoomDatabase
import com.example.todoya.data.sp.AuthTokenManager
import com.example.todoya.domain.manager.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


/**
 * Application class responsible for initializing dependencies
 */
class TodoApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { TodoRoomDatabase.getDatabase(this, applicationScope) }
    private val authTokenManager by lazy { AuthTokenManager(this) }
    private val syncManager by lazy { SyncManager(this) }

    val repository by lazy {
        val savedToken = authTokenManager.getAuthToken()
        TodoItemsRepositoryImpl(
            database.todoDao(),
            RetrofitInstance.api,
            "OAuth ${savedToken ?: ""}"
        )
    }

    fun updateAuthToken(token: String){
        authTokenManager.saveAuthToken(token)
        repository.updateAuthToken("OAuth $token")
        syncManager.scheduleSyncWork()
    }
}
