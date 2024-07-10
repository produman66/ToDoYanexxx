package com.example.todoya.data

import com.example.todoya.data.room.entity.Importance
import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.model.Task
import java.util.Date
import java.util.UUID


/**
 * Utility object for converting between TodoItem and Task.
 */
object TaskConverter {
    fun toTodoItem(task: Task): TodoItem {
        return TodoItem(
            id = task.id,
            text = task.text,
            importance = when (task.importance) {
                "low" -> Importance.LOW
                "important" -> Importance.HIGH
                else -> Importance.NO
            },
            deadline = if (task.deadline != 0) Date(task.deadline.toLong() * 1000) else null,
            isCompleted = task.done,
            createdAt = Date(task.created_at.toLong() * 1000),
            modifiedAt = if (task.changed_at != 0) Date(task.changed_at.toLong() * 1000) else null,
            isSynced = true,
            isModified = true,
            isDeleted = false
        )
    }

    fun toTask(todoItem: TodoItem): Task {
        return Task(
            id = todoItem.id,
            text = todoItem.text,
            importance = when (todoItem.importance) {
                Importance.LOW -> "low"
                Importance.HIGH -> "important"
                else -> "basic"
            },
            deadline = todoItem.deadline?.time?.div(1000)?.toInt() ?: 0,
            done = todoItem.isCompleted,
            color = "#FFFFFF",
            created_at = todoItem.createdAt.time.div(1000).toInt(),
            changed_at = todoItem.modifiedAt?.time?.div(1000)?.toInt()
                ?: todoItem.createdAt.time.div(1000).toInt(),
            last_updated_by = UUID.randomUUID().toString()
        )
    }
}