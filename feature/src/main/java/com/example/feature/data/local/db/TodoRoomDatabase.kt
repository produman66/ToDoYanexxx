package com.example.feature.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.feature.data.local.dao.TodoDao
import com.example.feature.data.local.model.DateConverter
import com.example.feature.data.local.model.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Singleton class representing the Room database for the Todo application.
 */
@Database(entities = [TodoItem::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class TodoRoomDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        @Volatile
        private var INSTANCE: TodoRoomDatabase? = null

        fun getDatabase(
            context: Context
        ): TodoRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoRoomDatabase::class.java,
                    "todo_database67"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class TodoDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.todoDao())
                }
            }
        }

        suspend fun populateDatabase(todoDao: TodoDao) {

        }
    }
}

