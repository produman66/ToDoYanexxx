package com.example.todoya.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date


@Entity(tableName = "todo_items")
@TypeConverters(DateConverter::class)
data class TodoItem(
    @PrimaryKey val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date? = null,
    val isCompleted: Boolean,
    val createdAt: Date,
    val modifiedAt: Date? = null
)

enum class Importance {
    NO,
    LOW,
    HIGH
}