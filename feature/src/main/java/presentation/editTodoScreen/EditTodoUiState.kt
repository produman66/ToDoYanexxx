package presentation.editTodoScreen

import network.observer.ConnectivityObserver
import data.local.model.TodoItem

data class EditTodoUiState(
    val selectedTodoItem: TodoItem? = null,
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val errorCode: Int? = null
)