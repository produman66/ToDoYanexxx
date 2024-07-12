package com.example.todoya

import android.app.Application
import com.example.feature.data.auth.AuthTokenManager
import com.example.feature.data.local.dao.TodoDao
import com.example.feature.data.repository.TodoItemsRepositoryImpl
import com.example.todoya.syncWork.SyncManager
import dagger.hilt.android.HiltAndroidApp
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
