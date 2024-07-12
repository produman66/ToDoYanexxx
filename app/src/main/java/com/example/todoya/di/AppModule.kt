package com.example.todoya.di

import android.content.Context
import com.example.todoya.syncWork.SyncManager
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
    fun provideSyncManager(
        @ApplicationContext context: Context
    ): SyncManager {
        return SyncManager(context)
    }

    @Provides
    @Singleton
    fun provideYandexAuthSdk(@ApplicationContext context: Context): YandexAuthSdk {
        return YandexAuthSdk.create(YandexAuthOptions(context))
    }

}