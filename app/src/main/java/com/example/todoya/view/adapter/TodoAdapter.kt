package com.example.todoya.view.adapter

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoya.R
import com.example.todoya.data.room.Importance
import com.example.todoya.data.room.TodoItem
import com.example.todoya.databinding.TodoItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var todoList = ArrayList<TodoItem>()
    private var onClickListener: OnClickListener? = null

    private val TYPE_ITEM = 0
    private val TYPE_BUTTON = 1

    fun setTodoList(newTodoList: List<TodoItem>) {
        val diffCallback = TodoDiffCallback(todoList, newTodoList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        todoList.clear()
        todoList.addAll(newTodoList)
        todoList.add(TodoItem(id = "", text = "", importance = Importance.NO, null,false ,createdAt = Date()))
        diffResult.dispatchUpdatesTo(this)
    }


    class ViewHolder(val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root)


    class ButtonViewHolder(val binding: View) : RecyclerView.ViewHolder(binding)


    override fun getItemViewType(position: Int): Int {
        return if (position == todoList.size - 1) TYPE_BUTTON else TYPE_ITEM
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.button_item, parent, false)
            ButtonViewHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val todo = todoList[position]
            val context = holder.itemView.context
            val importance = todo.importance

            holder.binding.checkBox.isChecked = todo.isCompleted

            holder.binding.textTodo.apply {
                text = todo.text

                if (todo.isCompleted) {
                    setTextColor(ContextCompat.getColor(context, R.color.grey))
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    setTextAppearance(R.style.Text_Body)
                }
            }

            if (todo.deadline != null) {
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))
                val formattedDate = dateFormat.format(todo.deadline)
                holder.binding.deadlineTodo.text = formattedDate
                holder.binding.deadlineTodo.visibility = View.VISIBLE
            } else {
                holder.binding.deadlineTodo.visibility = View.GONE
            }

            when (importance) {
                Importance.LOW -> {
                    holder.binding.importanceTodo.visibility = View.VISIBLE
                    holder.binding.importanceTodo.setImageResource(R.drawable.arrow_down)
                }
                Importance.HIGH -> {
                    holder.binding.importanceTodo.visibility = View.VISIBLE
                    holder.binding.importanceTodo.setImageResource(R.drawable.importance_high)

                    val states = arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                    )

                    val colors = intArrayOf(
                        ContextCompat.getColor(context, R.color.green),
                        ContextCompat.getColor(context, R.color.red)
                    )

                    val colorStateList = ColorStateList(states, colors)
                    holder.binding.checkBox.buttonTintList = colorStateList
                }
                else -> {
                    holder.binding.importanceTodo.visibility = View.GONE

                    val states = arrayOf(
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(-android.R.attr.state_checked)
                    )

                    val colors = intArrayOf(
                        ContextCompat.getColor(context, R.color.green),
                        ContextCompat.getColor(context, R.color.grey)
                    )

                    val colorStateList = ColorStateList(states, colors)
                    holder.binding.checkBox.buttonTintList = colorStateList
                }
            }

            holder.binding.checkBox.setOnClickListener {
                onClickListener?.onClickCheckBox(position, todo)
            }

            holder.binding.itemRecycler.setOnClickListener {
                onClickListener?.onClick(position, todo)
            }
        } else if (holder is ButtonViewHolder) {
            holder.binding.setOnClickListener {
                onClickListener?.onClickButton()
            }
        }
    }


    override fun getItemCount(): Int {
        return todoList.size
    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}
