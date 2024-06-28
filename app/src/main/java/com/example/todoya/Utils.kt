package com.example.todoya

import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    private val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
}