package presentation.homeScreen


import Utils.getNetworkStatusMessage
import Utils.showErrorSnackbar
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import com.example.feature.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import data.local.model.Importance
import data.local.model.TodoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import network.observer.ConnectivityObserver
import presentation.navigation.MainDestinations
import theme.TodoYaTheme
import java.util.Date


/**
 * Home screen that displays a list of todo items and various UI components.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    navController: NavController,
    onToggleCompletedById: (String) -> Unit,
    onClearError: () -> Unit,
    onInitializeConnectivityObserver: (Context) -> Unit,
    onToggleEyeClosed: () -> Unit,
    onSyncWithServer: () -> Unit,
    onSettingsClick: () -> Unit,
    onInfoClick: () -> Unit,
    onDeleteTodoById: (String) -> Unit,
    onUndoTodoById: (String) -> Unit,
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }
    val swipeRefreshState = remember { SwipeRefreshState(false) }

    LaunchedEffect(uiState.undoList) {
        if (uiState.undoList != null){
            val result = snackbarHostState.showSnackbar(
                message = "Удалить ${uiState.undoList.text}",
                actionLabel = "Отмена",
                duration = SnackbarDuration.Indefinite
            )
            when (result) {
                SnackbarResult.Dismissed -> {
                    onUndoTodoById(uiState.undoList.id)
                }

                SnackbarResult.ActionPerformed -> {
                    onUndoTodoById(uiState.undoList.id)
                    onDeleteTodoById(uiState.undoList.id)
                }
            }
        }
    }

    uiState.errorCode?.let { error ->
        LaunchedEffect(error) {
                showErrorSnackbar(error, snackbarHostState, scope, onSyncWithServer)
                onClearError()
        }
    }

    LaunchedEffect(Unit) {
            onInitializeConnectivityObserver(context)
    }

    LaunchedEffect(uiState.networkStatus) {
            delay(5000)
            val message = getNetworkStatusMessage(uiState, onSyncWithServer)
            snackbarHostState.showSnackbar(message)
    }


    LaunchedEffect(swipeRefreshState.isRefreshing) {
        if (swipeRefreshState.isRefreshing) {
            val message = getNetworkStatusMessage(uiState, onSyncWithServer)
            snackbarHostState.showSnackbar(message)
            swipeRefreshState.isRefreshing = false
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) { data ->
                CountdownSnackbar(data)
            }
        },

        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(color = MaterialTheme.colorScheme.primary),
        topBar = {
            TodoTopBar(
                scrollBehavior = scrollBehavior,
                isCollapsed = scrollBehavior.state.collapsedFraction == 1f,
                isEyeClosed = uiState.isEyeClosed,
                countCompletedTodo = uiState.countCompletedTodo,
                onEyeToggle = onToggleEyeClosed,
                onSettingsClick = onSettingsClick,
                onInfoClick = onInfoClick
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
                        items(uiState.curItemsList, key = { it.id }) { item: TodoItem ->
                            TodoItemScreen(item = item, onCheckedChange = {
                                onToggleCompletedById(item.id)
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
    val fakeTodoItems = listOf(
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
            isDeleted = false,
            isUndo = false
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
            isDeleted = false,
            isUndo = false
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
            isDeleted = false,
            isUndo = false
        )
    )

    val fakeUiState = HomeScreenUiState(
        curItemsList = fakeTodoItems,
        countCompletedTodo = 1,
        errorCode = null,
        isEyeClosed = false,
        networkStatus = ConnectivityObserver.Status.Available
    )

    val navController = rememberNavController()

    TodoYaTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                uiState = fakeUiState,
                navController = navController,
                onToggleCompletedById = {},
                onClearError = {},
                onInitializeConnectivityObserver = {},
                onToggleEyeClosed = {},
                onSyncWithServer = {},
                onSettingsClick = {},
                onInfoClick = {},
                onDeleteTodoById = {},
                onUndoTodoById = {},
            )
        }
    }
}


/**
 * Preview of the home screen in dark mode.
 */
@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun HomeScreenPreviewDark() {
    val fakeTodoItems = listOf(
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
            isDeleted = false,
            isUndo = false
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
            isDeleted = false,
            isUndo = false
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
            isDeleted = false,
            isUndo = false
        )
    )

    val fakeUiState = HomeScreenUiState(
        curItemsList = fakeTodoItems,
        countCompletedTodo = 1,
        errorCode = null,
        isEyeClosed = false,
        networkStatus = ConnectivityObserver.Status.Available
    )

    val navController = rememberNavController()

    TodoYaTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                uiState = fakeUiState,
                navController = navController,
                onToggleCompletedById = {},
                onClearError = {},
                onInitializeConnectivityObserver = {},
                onToggleEyeClosed = {},
                onSyncWithServer = {},
                onSettingsClick = {},
                onInfoClick = {},
                onDeleteTodoById = {},
                onUndoTodoById = {},
            )
        }
    }
}





