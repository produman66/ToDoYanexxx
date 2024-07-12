package com.example.feature.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date


/**
 * Entity class representing a TodoItem in the database.
 */
@Entity(tableName = "todo_items")
@TypeConverters(DateConverter::class)
data class TodoItem(
    @PrimaryKey val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date? = null,
    val isCompleted: Boolean,
    val createdAt: Date,
    val modifiedAt: Date? = null,
    val isSynced: Boolean,
    val isModified: Boolean,
    val isDeleted: Boolean
)


/**
 * Enum class representing the importance levels of a TodoItem.
 */
enum class Importance {
    NO,
    LOW,
    HIGH
}
