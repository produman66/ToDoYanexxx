package com.example.todoya.view.adapter

import com.example.todoya.data.room.TodoItem


interface OnClickListener {
    fun onClick(position: Int, model: TodoItem)

    fun onClickCheckBox(position: Int, model: TodoItem)

    fun onClickButton()
}
