package com.example.todoya.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoya.R
import com.example.todoya.view.adapter.OnClickListener
import com.example.todoya.view.adapter.TodoAdapter
import com.example.todoya.data.entity.TodoItem
import com.example.todoya.databinding.FragmentMainScreenBinding
import com.google.android.material.appbar.AppBarLayout


class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val bindingMS get() = _binding!!

    private val todoAdapter = TodoAdapter()
//    private val todoViewModel: TodoViewModel by viewModels {
//        TodoViewModelFactory((requireActivity() as MainActivity).repository)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
//        Log.d("MainFragment", "${todoViewModel.isEyeClosed}")
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return bindingMS.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCorrectEye()
        setupRecyclerView()
        setupFab()
        setupEyeButtons()
        setupAppBarLayout()

        todoAdapter.setOnClickListener(object : OnClickListener {
            override fun onClick(position: Int, model: TodoItem) {
                val id = model.id
                val bundle = bundleOf("todoId" to id)
                findNavController().navigate(
                    R.id.action_mainScreenFragment_to_newTaskFragment,
                    bundle
                )
            }

            override fun onClickCheckBox(position: Int, model: TodoItem) {
                val id = model.id
//                todoViewModel.toggleCompletedById(id)
                observeCompletedCount()
            }

            override fun onClickButton() {
                val bundle = bundleOf("todoId" to "")
                findNavController().navigate(R.id.action_mainScreenFragment_to_newTaskFragment, bundle)
            }
        })

        observeViewModel()
        observeCompletedCount()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        bindingMS.todoRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todoAdapter
        }
    }


    private fun setupFab() {
        bindingMS.fab.setOnClickListener {
            val bundle = bundleOf("todoId" to "")
            findNavController().navigate(R.id.action_mainScreenFragment_to_newTaskFragment, bundle)
        }
    }


    private fun setupEyeButtons() {
        bindingMS.eyeTodo.setOnClickListener { toggleEyeState() }
        bindingMS.eyeToolbar.setOnClickListener { toggleEyeState() }
    }


    private fun setupAppBarLayout() {
        bindingMS.appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShown = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    showToolbar()
                    isShown = true
                } else if (isShown) {
                    hideToolbar()
                    isShown = false
                }
            }
        })
    }


    private fun showToolbar() {
        bindingMS.titleToolbar.visibility = View.VISIBLE
        bindingMS.eyeToolbar.visibility = View.VISIBLE
    }


    private fun hideToolbar() {
        bindingMS.titleToolbar.visibility = View.GONE
        bindingMS.eyeToolbar.visibility = View.GONE
    }


    private fun toggleEyeState() {
//        todoViewModel.isEyeClosed = !todoViewModel.isEyeClosed
        updateTodoList()
    }


    private fun observeViewModel() {
//        todoViewModel.allTodo.observe(viewLifecycleOwner, Observer { todo ->
//            if (todoViewModel.isEyeClosed) {
//                todo?.let { todoAdapter.setTodoList(it) }
//            }
//        })

//        todoViewModel.todoIncomplete.observe(viewLifecycleOwner, Observer { todo ->
//            if (!todoViewModel.isEyeClosed) {
//                todo?.let { todoAdapter.setTodoList(it) }
//            }
//        })
    }


    private fun updateTodoList() {
//        if (todoViewModel.isEyeClosed) {
//            bindingMS.eyeTodo.setImageResource(R.drawable.visibility_off)
//            bindingMS.eyeToolbar.setImageResource(R.drawable.visibility_off)
//            todoViewModel.allTodo.value?.let { todoAdapter.setTodoList(it) }
//        } else {
//            bindingMS.eyeTodo.setImageResource(R.drawable.visibility)
//            bindingMS.eyeToolbar.setImageResource(R.drawable.visibility)
//            todoViewModel.todoIncomplete.value?.let { todoAdapter.setTodoList(it) }
//        }
    }


    private fun observeCompletedCount() {
//        todoViewModel.getCompletedTodoCount().observe(viewLifecycleOwner) { count ->
//            bindingMS.completedTodo.text = getString(R.string.completed_todo_text, count)
//        }
    }


    private fun setupCorrectEye() {
//        if (todoViewModel.isEyeClosed) {
//            bindingMS.eyeTodo.setImageResource(R.drawable.visibility_off)
//            bindingMS.eyeToolbar.setImageResource(R.drawable.visibility_off)
//        } else {
//            bindingMS.eyeTodo.setImageResource(R.drawable.visibility)
//            bindingMS.eyeToolbar.setImageResource(R.drawable.visibility)
//        }
    }
}
