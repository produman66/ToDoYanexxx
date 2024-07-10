package com.example.todoya

import android.app.Application
import com.example.todoya.data.repository.TodoItemsRepositoryImpl
import com.example.todoya.data.retrofit.RetrofitInstance
import com.example.todoya.data.room.dao.TodoDao
import com.example.todoya.data.room.db.TodoRoomDatabase
import com.example.todoya.data.sp.AuthTokenManager
import com.example.todoya.domain.manager.SyncManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject


/**
 * Application class responsible for initializing dependencies
 */
@HiltAndroidApp
class TodoApplication : Application() {

    @Inject
    lateinit var todoDao: TodoDao

    @Inject
    lateinit var authTokenManager: AuthTokenManager

    @Inject
    lateinit var syncManager: SyncManager

    @Inject
    lateinit var repository: TodoItemsRepositoryImpl

    fun updateAuthToken(token: String){
        authTokenManager.saveAuthToken(token)
        repository.updateAuthToken("OAuth $token")
        syncManager.scheduleSyncWork()
    }
}
