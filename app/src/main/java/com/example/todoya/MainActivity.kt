package com.example.todoya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todoya.databinding.ActivityMainBinding
import com.example.todoya.repository.TodoItemsRepository

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val bindingAM get() = _binding!!

    val repository: TodoItemsRepository
        get() = (application as TodoApplication).repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingAM.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}