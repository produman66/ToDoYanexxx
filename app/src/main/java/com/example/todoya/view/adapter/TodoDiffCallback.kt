package com.example.todoya.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todoya.data.entity.TodoItem

class TodoDiffCallback(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}