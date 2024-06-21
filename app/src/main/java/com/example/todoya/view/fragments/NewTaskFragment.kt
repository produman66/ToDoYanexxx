package com.example.todoya.view.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoya.MainActivity
import com.example.todoya.R
import com.example.todoya.Utils
import com.example.todoya.data.room.Importance
import com.example.todoya.data.room.TodoItem
import com.example.todoya.databinding.FragmentNewTaskBinding
import com.example.todoya.viewmodel.TodoViewModel
import com.example.todoya.viewmodel.TodoViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class NewTaskFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentNewTaskBinding? = null
    private val bindingNT get() = _binding!!

    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((requireActivity() as MainActivity).repository)
    }

    private var todoId = ""
    private var selectedDeadline: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoId = arguments?.getString("todoId").orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        return bindingNT.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupSpinner()
        setupListeners()

        if (todoId.isNotEmpty()) {
            loadTodoItem()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupToolbar() {
        val toolbar = bindingNT.toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.title = ""
    }


    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.importance_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bindingNT.importanceTodo.adapter = adapter
        }
        bindingNT.importanceTodo.onItemSelectedListener = this
    }


    private fun setupListeners() {
        bindingNT.save.setOnClickListener { saveTodoItem() }
        bindingNT.close.setOnClickListener { findNavController().popBackStack() }
        bindingNT.materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchChange(isChecked)
        }
    }


    private fun loadTodoItem() {
        todoViewModel.getTodoById(todoId).observe(viewLifecycleOwner) { todoItem ->
            todoItem?.let { populateFields(it) }
        }
    }


    private fun populateFields(todoItem: TodoItem) {
        bindingNT.editText.setText(todoItem.text)
        bindingNT.importanceTodo.setSelection(todoItem.importance.ordinal)

        todoItem.deadline?.let {
            val formattedDate = Utils.formatDate(it)
            selectedDeadline = it
            bindingNT.deadlineTodo.text = formattedDate
            bindingNT.deadlineTodo.visibility = View.VISIBLE
            bindingNT.materialSwitch.isChecked = true
        }

        val color = ContextCompat.getColor(requireContext(), R.color.red)
        bindingNT.deleteImg.setColorFilter(color)
        bindingNT.deleteTxt.setTextColor(color)

        bindingNT.deleteTodo.setOnClickListener {
            todoViewModel.deleteTodoById(todoId)
            findNavController().popBackStack()
        }
    }


    private fun saveTodoItem() {
        val todoText = bindingNT.editText.text.toString()
        if (todoText.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.toast_write_something), Toast.LENGTH_SHORT).show()
            return
        }

        val importance = when (bindingNT.importanceTodo.selectedItemPosition) {
            1 -> Importance.LOW
            2 -> Importance.HIGH
            else -> Importance.NO
        }

        val newTodo = TodoItem(
            id = todoId.ifEmpty { Random().nextLong().toString() },
            text = todoText,
            importance = importance,
            deadline = selectedDeadline,
            isCompleted = false,
            createdAt = Date(System.currentTimeMillis()),
            modifiedAt = null
        )

        todoViewModel.insert(newTodo)
        findNavController().popBackStack()
    }


    private fun handleSwitchChange(isChecked: Boolean) {
        if (isChecked) {
            if (selectedDeadline == null) {
                showDatePicker()
            }
        } else {
            selectedDeadline = null
            bindingNT.deadlineTodo.text = ""
            bindingNT.deadlineTodo.visibility = View.GONE
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val textColor = if (position == 2) {
            R.color.red
        } else {
            R.color.iconColor
        }
        (view as? TextView)?.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {}


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val currentLocale = Locale.getDefault()

        Locale.setDefault(Locale("ru", "RU"))
        val configuration = requireContext().resources.configuration
        configuration.setLocale(Locale("ru", "RU"))
        requireContext().createConfigurationContext(configuration)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePicker,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)

                val formattedDate = Utils.formatDate(calendar.time)

                bindingNT.deadlineTodo.text = formattedDate
                bindingNT.deadlineTodo.visibility = View.VISIBLE
                selectedDeadline = calendar.time

                Locale.setDefault(currentLocale)
                val config = requireContext().resources.configuration
                config.setLocale(currentLocale)
                requireContext().createConfigurationContext(config)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
