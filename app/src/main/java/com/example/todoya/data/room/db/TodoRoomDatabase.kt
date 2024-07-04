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
                    "todo_database61"
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
//                id = UUID.randomUUID().toString(),
//                text = "Meditate for 10 minutes",
//                importance = Importance.HIGH,
//                deadline = null,
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Walk the dog",
//                importance = Importance.LOW,
//                deadline = null,
//                isCompleted = true,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Buy groceries",
//                importance = Importance.NO,
//                deadline = Date(System.currentTimeMillis() + 86400000), // 1 day from now
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Call mom",
//                importance = Importance.HIGH,
//                deadline = Date(System.currentTimeMillis() + 604800000), // 7 days from now
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Submit tax documents",
//                importance = Importance.HIGH,
//                deadline = null,
//                isCompleted = true,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false // Marked as deleted
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Finish reading book",
//                importance = Importance.LOW,
//                deadline = null,
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Prepare presentation",
//                importance = Importance.NO,
//                deadline = Date(System.currentTimeMillis() + 2592000000), // 30 days from now
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Clean the house",
//                importance = Importance.LOW,
//                deadline = Date(System.currentTimeMillis() + 172800000), // 2 days from now
//                isCompleted = true,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Workout for 30 minutes",
//                importance = Importance.HIGH,
//                deadline = null,
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)
//
//            todo = TodoItem(
//                id = UUID.randomUUID().toString(),
//                text = "Plan vacation",
//                importance = Importance.NO,
//                deadline = Date(System.currentTimeMillis() + 1209600000), // 14 days from now
//                isCompleted = false,
//                createdAt = Date(),
//                modifiedAt = Date(),
//                isSynced = false,
//                isDeleted = false
//            )
//            todoDao.insert(todo)

        }
    }
}
