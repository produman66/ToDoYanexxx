package di

import android.content.Context
import data.local.dao.TodoDao
import data.local.db.TodoRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
}