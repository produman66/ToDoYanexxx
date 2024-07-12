package com.example.todoya.data.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoya.data.room.dao.TodoDao
import com.example.todoya.data.room.entity.DateConverter
import com.example.todoya.data.room.entity.TodoItem
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
            context: Context,
            scope: CoroutineScope
        ): TodoRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoRoomDatabase::class.java,
                    "todo_database66"
                )
                    .addCallback(TodoDatabaseCallback(scope))
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
//            var todo = TodoItem(
//                id = "7ec71b48-4346-4cad-9659-897b7e1c8da3",
//                text = "Meditate for 10 minutes",
//                importance = Importance.HIGH,
//                deadline = null,
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = true,
//                isModified = true,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
        }
    }
}
