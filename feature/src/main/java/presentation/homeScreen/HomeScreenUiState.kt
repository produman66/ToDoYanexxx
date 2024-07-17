package presentation.homeScreen


import network.observer.ConnectivityObserver
import data.local.model.TodoItem


data class HomeScreenUiState(
    val curItemsList: List<TodoItem> = emptyList(),
    val countCompletedTodo: Int = 0,
    val isEyeClosed: Boolean = false,
    val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val errorCode: Int? = null
)