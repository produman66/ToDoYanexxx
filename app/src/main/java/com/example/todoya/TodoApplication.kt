package com.example.todoya

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import data.auth.AuthTokenManager
import data.local.dao.TodoDao
import data.repository.TodoItemsRepositoryImpl
import com.example.todoya.syncWork.SyncManagerImpl
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import presentation.settingsScreen.SettingsRepository
import presentation.settingsScreen.ThemeMode
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
