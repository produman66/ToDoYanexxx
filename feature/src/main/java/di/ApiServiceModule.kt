package di

import data.remote.retrofit.RetrofitInstance
import data.remote.retrofit.TodoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Provides
    @Singleton
    fun provideTodoApiService(): TodoApiService {
        return RetrofitInstance.api
    }

}