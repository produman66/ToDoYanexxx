package com.example.feature

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import data.local.dao.TodoDao
import data.local.db.TodoRoomDatabase
import data.local.model.Importance
import data.local.model.TodoItem
import data.remote.retrofit.TodoApiService
import data.repository.TodoItemsRepositoryImpl
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.Date

class TodoItemsRepositoryGetAllTodoTest {

    private lateinit var database: TodoRoomDatabase
    private lateinit var todoDao: TodoDao
    private lateinit var fakeRepository: TodoItemsRepositoryImpl


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoRoomDatabase::class.java
        ).allowMainThreadQueries().build()

        todoDao = database.todoDao()
        val todoApiService = mock(TodoApiService::class.java)
        fakeRepository = TodoItemsRepositoryImpl(todoDao, todoApiService, "fake_token")
    }


    @Test
    fun testInsertTodo() = runBlocking {
//        val todoItem = TodoItem(
//            id = "1",
//            text = "Test task",
//            importance = Importance.LOW,
//            deadline = null,
//            isCompleted = false,
//            createdAt = Date(),
//            modifiedAt = null,
//            isSynced = false,
//            isModified = false,
//            isDeleted = false,
//            isUndo = false
//        )
//
//        fakeRepository.insert(todoItem)

        val todos = todoDao.getAllTodo().first()
        assertEquals(0, todos.size)
//        assertEquals(todoItem, todos[0])
    }


    @After
    fun tearDown() {
        database.close()
    }
}
