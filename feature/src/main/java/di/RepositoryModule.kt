package di

import data.auth.AuthTokenManager
import data.local.dao.TodoDao
import data.remote.retrofit.TodoApiService
import data.repository.TodoItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

}