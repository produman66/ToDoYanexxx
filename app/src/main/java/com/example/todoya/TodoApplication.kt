package com.example.todoya

import android.app.Application
import com.example.todoya.syncWork.SyncManagerImpl
import dagger.hilt.android.HiltAndroidApp
import data.auth.AuthTokenManager
import data.local.dao.TodoDao
import data.repository.TodoItemsRepositoryImpl
import presentation.settingsScreen.SettingsRepository
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
    lateinit var syncManager: SyncManagerImpl

    @Inject
    lateinit var repository: TodoItemsRepositoryImpl

    @Inject
    lateinit var settingsRepository: SettingsRepository


    fun updateAuthToken(token: String){
        authTokenManager.saveAuthToken(token)
        repository.updateAuthToken("OAuth $token")
        syncManager.scheduleSyncWork()
    }
}
