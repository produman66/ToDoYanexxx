package com.example.todoya
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.example.todoya.data.room.entity.Importance
import com.example.todoya.domain.repository.ConnectivityObserver
import com.example.todoya.presentation.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    private val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru", "RU"))


    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun showErrorSnackbar(errorCode: Int?, snackbarHostState: SnackbarHostState, scope: CoroutineScope, todoViewModel: TodoViewModel) {
        val message = when (errorCode) {
            1 -> "Элемент не добавлен! Непонятная ошибка"
            2 -> "Элемент не удален! Непонятная ошибка"
            3 -> "Стасус элемента не поменялся! Непонятная ошибка"
            4 -> "Ошибка синхронизации данных с сервером!"
            5 -> "Не получилось удалить элемент с сервера!"
            6 -> "Ошибка подключения"
            7 -> "Ошибка получения данные с сервера!"
            else -> "Код ошибки: $errorCode"
        }
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Повторить"
            )
            when (result) {
                SnackbarResult.ActionPerformed -> { todoViewModel.syncWithServer() }
                SnackbarResult.Dismissed -> {}
            }
        }
    }

    fun getNetworkStatusMessage(
        networkStatus: ConnectivityObserver.Status,
        todoViewModel: TodoViewModel
    ): String {
        return when (networkStatus) {
            ConnectivityObserver.Status.Available -> {
                todoViewModel.syncWithServer()
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