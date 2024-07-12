package com.example.feature

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.example.core.network_manager.ConnectivityObserver
import com.example.feature.data.local.model.Importance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    private val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))


    fun formatDate(date: Date?): String {
        return dateFormat.format(date!!)
    }

    fun showErrorSnackbar(
        errorCode: Int?,
        snackbarHostState: SnackbarHostState,
        scope: CoroutineScope,
        onSyncWithServer: () -> Unit
    ) {
        val message = when (errorCode) {
            1 -> "Элемент не добавлен! Непонятная ошибка"
            2 -> "Элемент не удален! Непонятная ошибка"
            3 -> "Стасус элемента не поменялся! Непонятная ошибка"
            4 -> "Ошибка синхронизации данных с сервером!"
            5 -> "Не получилось удалить элемент с сервера!"
            6 -> "Ошибка подключения"
            7 -> "Ошибка получения данных с сервера!"
            else -> "Код ошибки: $errorCode"
        }
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Повторить"
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    onSyncWithServer()
                }

                SnackbarResult.Dismissed -> {}
            }
        }
    }

    fun getNetworkStatusMessage(
        networkStatus: ConnectivityObserver.Status,
        onSyncWithServer: () -> Unit
    ): String {
        return when (networkStatus) {
            ConnectivityObserver.Status.Available -> {
                onSyncWithServer()
                "Данные синхронизируются с сервером"
            }

            ConnectivityObserver.Status.Unavailable -> "Сеть недоступна! Все изменения происходят локально"
            ConnectivityObserver.Status.Losing -> "Сеть теряет соединение"
            ConnectivityObserver.Status.Lost -> "Сеть потеряна. Данные сохраняются локально"
        }
    }


    fun mapSelectedOptionToImportance(option: String): Importance {
        return when (option) {
            "Низкий" -> Importance.LOW
            "Высокий" -> Importance.HIGH
            else -> Importance.NO
        }
    }
}