package com.example.todoya.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todoya.R
import com.example.todoya.Utils
import com.example.todoya.Utils.showErrorSnackbar
import com.example.todoya.data.room.entity.Importance
import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.domain.repository.TodoItemsRepository
import com.example.todoya.presentation.navigation.MainDestinations
import com.example.todoya.presentation.viewmodel.TodoViewModel
import com.example.todoya.ui.theme.TodoYaTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Date


/**
 * Home screen that displays a list of todo items and various UI components.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    todoViewModel: TodoViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val countCompletedTodo by todoViewModel.getCompletedTodoCount().observeAsState()
    val isEyeClosed by todoViewModel.isEyeClosed.collectAsState()
    val listState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val curItemsList: State<List<TodoItem>> = if (isEyeClosed) {
        todoViewModel.allTodo.collectAsState(listOf())
    } else {
        todoViewModel.todoIncomplete.collectAsState(listOf())
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    val snackbarHostState = remember { SnackbarHostState() }

    val error by todoViewModel.errorCode.collectAsState()

    error?.let {
        LaunchedEffect(it) {
            showErrorSnackbar(it, snackbarHostState, scope, todoViewModel)
            todoViewModel.clearError()
        }
    }

    val swipeRefreshState = remember { SwipeRefreshState(false) }

    LaunchedEffect(Unit) {
        todoViewModel.initializeConnectivityObserver(context)
    }

    val networkStatus by todoViewModel.networkStatus.collectAsState()

    LaunchedEffect(networkStatus) {
        val message =  Utils.getNetworkStatusMessage(networkStatus, todoViewModel)
        snackbarHostState.showSnackbar(message)
    }


    LaunchedEffect(swipeRefreshState.isRefreshing) {
        if (swipeRefreshState.isRefreshing) {
            val message = Utils.getNetworkStatusMessage(networkStatus, todoViewModel)
            snackbarHostState.showSnackbar(message)
            swipeRefreshState.isRefreshing = false

        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(color = MaterialTheme.colorScheme.primary),
        topBar = {
            TodoTopBar(
                scrollBehavior = scrollBehavior,
                isCollapsed = scrollBehavior.state.collapsedFraction == 1f,
                todoViewModel = todoViewModel,
                isEyeClosed = isEyeClosed,
                countCompletedTodo = countCompletedTodo
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.tertiary,
                shape = CircleShape,
                onClick = {
                    navController.navigate("${MainDestinations.ITEM_SCREEN}/ ")
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        tint = colorResource(id = R.color.white),
                        contentDescription = stringResource(id = R.string.add_task)
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    swipeRefreshState.isRefreshing = true
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp)
                        .shadow(4.dp, RoundedCornerShape(8.dp)),
                    state = listState,
                    content = {
                        items(curItemsList.value, key = { it.id }) { item: TodoItem ->
                            TodoItemScreen(item = item, onCheckedChange = {
                                todoViewModel.toggleCompletedById(item.id)
                            }) {
                                navController.navigate("${MainDestinations.ITEM_SCREEN}/${item.id}")
                            }
                        }
                        item {
                            TodoNew {
                                navController.navigate("${MainDestinations.ITEM_SCREEN}/ ")
                            }
                        }
                    }
                )
            }
        }
    }
}


/**
 * Preview of the home screen in light mode.
 */
@Preview(showBackground = true, name = "Light Theme")
@Composable
fun HomeScreenPreviewLight() {
    val fakeTodoItemsRepository = object : TodoItemsRepository {
        override val allTodo: Flow<List<TodoItem>> = flowOf(
            listOf(
                TodoItem(
                    id = "2",
                    text = "Read a book",
                    importance = Importance.LOW,
                    deadline = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null,
                    isSynced = false,
                    isModified = false,
                    isDeleted = false
                ),
                TodoItem(
                    id = "3",
                    text = "Read a book",
                    importance = Importance.HIGH,
                    deadline = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null,
                    isSynced = false,
                    isModified = true,
                    isDeleted = false
                ),
                TodoItem(
                    id = "4",
                    text = "Read a book",
                    importance = Importance.NO,
                    deadline = Date(System.currentTimeMillis() + 172800000),
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null,
                    isSynced = false,
                    isModified = false,
                    isDeleted = false
                )
            )
        )
        override val incompleteTodo: Flow<List<TodoItem>> = allTodo.map { todoList ->
            todoList.filter { !it.isCompleted }
        }

        override suspend fun insert(todo: TodoItem) {}
        override suspend fun deleteTodoById(id: String) {}
        override suspend fun toggleCompletedById(id: String) {}
        override suspend fun getTodoById(id: String): TodoItem? {
            return null
        }

        override fun getCompletedTodoCount(): Flow<Int> = flowOf(1)
        override suspend fun syncWithServer() {}
        override suspend fun getServerRevision(): Int {
            return 0
        }
    }

    val fakeTodoViewModel = TodoViewModel(fakeTodoItemsRepository)

    val navController = rememberNavController()

    TodoYaTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(todoViewModel = fakeTodoViewModel, navController = navController)
        }
    }
}


/**
 * Preview of the home screen in dark mode.
 */
@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun HomeScreenPreviewDark() {
    val fakeTodoItemsRepository = object : TodoItemsRepository {
        override val allTodo: Flow<List<TodoItem>> = flowOf(
            listOf(
                TodoItem(
                    id = "2",
                    text = "Read a book",
                    importance = Importance.LOW,
                    deadline = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null,
                    isSynced = false,
                    isModified = false,
                    isDeleted = false
                ),
                TodoItem(
                    id = "3",
                    text = "Read a book",
                    importance = Importance.HIGH,
                    deadline = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null,
                    isSynced = false,
                    isModified = false,
                    isDeleted = false
                ),
                TodoItem(
                    id = "4",
                    text = "Read a book",
                    importance = Importance.NO,
                    deadline = Date(System.currentTimeMillis() + 172800000),
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null,
                    isSynced = false,
                    isModified = false,
                    isDeleted = false
                )
            )
        )
        override val incompleteTodo: Flow<List<TodoItem>> = allTodo.map { todoList ->
            todoList.filter { !it.isCompleted }
        }

        override suspend fun insert(todo: TodoItem) {}
        override suspend fun deleteTodoById(id: String) {}
        override suspend fun toggleCompletedById(id: String) {}
        override suspend fun getTodoById(id: String): TodoItem? {
            return null
        }

        override fun getCompletedTodoCount(): Flow<Int> = flowOf(1)
        override suspend fun syncWithServer() {}
        override suspend fun getServerRevision(): Int {
            return 0
        }
    }

    val fakeTodoViewModel = TodoViewModel(fakeTodoItemsRepository)

    val navController = rememberNavController()

    TodoYaTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(todoViewModel = fakeTodoViewModel, navController = navController)
        }
    }
}






