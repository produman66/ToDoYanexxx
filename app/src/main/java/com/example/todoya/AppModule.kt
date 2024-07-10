package com.example.todoya

import android.content.Context
import com.example.todoya.data.repository.TodoItemsRepositoryImpl
import com.example.todoya.data.retrofit.RetrofitInstance
import com.example.todoya.data.retrofit.TodoApiService
import com.example.todoya.data.room.dao.TodoDao
import com.example.todoya.data.room.db.TodoRoomDatabase
import com.example.todoya.data.sp.AuthManager
import com.example.todoya.data.sp.AuthTokenManager
import com.example.todoya.domain.manager.SyncManager
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TodoRoomDatabase {
        return TodoRoomDatabase.getDatabase(context)
    }


    @Provides
    fun provideTodoDao(database: TodoRoomDatabase): TodoDao {
        return database.todoDao()
    }


    @Provides
    @Singleton
    fun provideAuthTokenManager(
        @ApplicationContext context: Context
    ): AuthTokenManager {
        return AuthTokenManager(context)
    }


    @Provides
    @Singleton
    fun provideSyncManager(
        @ApplicationContext context: Context
    ): SyncManager {
        return SyncManager(context)
    }


    @Provides
    @Singleton
    fun provideTodoApiService(): TodoApiService {
        return RetrofitInstance.api
    }


    @Provides
    @Singleton
    fun provideYandexAuthSdk(@ApplicationContext context: Context): YandexAuthSdk {
        return YandexAuthSdk.create(YandexAuthOptions(context))
    }


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


    @Provides
    @Singleton
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager {
        return AuthManager(context)
    }

}