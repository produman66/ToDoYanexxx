package di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import data.auth.AuthTokenManager
import data.local.dao.TodoDao
import data.remote.retrofit.TodoApiService
import data.repository.TodoItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import presentation.settingsScreen.SettingsRepository
import androidx.datastore.preferences.core.Preferences
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTodoItemsRepository(
        todoDao: TodoDao,
        todoApiService: TodoApiService,
        authTokenManager: AuthTokenManager
    ): TodoItemsRepositoryImpl {
        val savedToken = authTokenManager.getAuthToken()
        return TodoItemsRepositoryImpl(
            todoDao,
            todoApiService,
            "OAuth ${savedToken ?: ""}"
        )
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    fun provideSettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository {
        return SettingsRepository(dataStore)
    }
}