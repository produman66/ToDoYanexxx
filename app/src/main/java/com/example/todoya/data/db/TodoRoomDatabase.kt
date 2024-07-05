package com.example.todoya.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoya.data.entity.DateConverter
import com.example.todoya.data.dao.TodoDao
import com.example.todoya.data.entity.Importance
import com.example.todoya.data.entity.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

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
                    "todo_database12"
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
            var todo = TodoItem(
                id = "1",
                text = "Go for a walk",
                importance = Importance.NO,
                deadline = null,
                isCompleted = true,
                createdAt = Date(),
                modifiedAt = Date()
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "2",
                text = "Read a book",
                importance = Importance.NO,
                deadline = null,
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "3",
                text = "Finish project report",
                importance = Importance.NO,
                deadline = Date(System.currentTimeMillis() + 172800000),
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "4",
                text = "Call John",
                importance = Importance.LOW,
                deadline = Date(System.currentTimeMillis() + 172800000),
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "5",
                text = "Go for a run",
                importance = Importance.LOW,
                deadline = null,
                isCompleted = true,
                createdAt = Date(),
                modifiedAt = Date()
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "6",
                text = "Buy groceries",
                importance = Importance.HIGH,
                deadline = Date(System.currentTimeMillis() + 86400000),
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "7",
                text = "Meeting with boss",
                importance = Importance.HIGH,
                deadline = Date(System.currentTimeMillis()),
                isCompleted = true,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "8",
                text = "Doctor appointment",
                importance = Importance.LOW,
                deadline = Date(System.currentTimeMillis() + 604800000),
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "9",
                text = "Complete coding challenge",
                importance = Importance.HIGH,
                deadline = Date(System.currentTimeMillis() + 259200000),
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "10",
                text = "Send email to client",
                importance = Importance.NO,
                deadline = null,
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "11",
                text = "Хорошего дня",
                importance = Importance.HIGH,
                deadline = null,
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)

            todo = TodoItem(
                id = "12",
                text = "Погулять с собокай",
                importance = Importance.HIGH,
                deadline = null,
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = null
            )
            todoDao.insert(todo)
        }
    }
}
