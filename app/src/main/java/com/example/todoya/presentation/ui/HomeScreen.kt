package com.example.todoya.presentation.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todoya.R
import com.example.todoya.data.entity.Importance
import com.example.todoya.data.entity.Importance.*
import com.example.todoya.data.entity.TodoItem
import com.example.todoya.domain.repository.ITodoItemsRepository
import com.example.todoya.presentation.navigation.MainDestinations
import com.example.todoya.presentation.viewmodel.TodoViewModel
import com.example.todoya.ui.theme.TodoYaTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(
        todoViewModel: TodoViewModel,
        navController: NavController
    ) {

        val countCompletedTodo by todoViewModel.getCompletedTodoCount().observeAsState()
        val isEyeClosed by todoViewModel.isEyeClosed.observeAsState()
        val listState = rememberLazyListState()

        val curItemsList: State<List<TodoItem>> = if (isEyeClosed == true) {
            todoViewModel.allTodo.observeAsState(listOf())
        } else {
            todoViewModel.todoIncomplete.observeAsState(listOf())
        }

        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .background(color = MaterialTheme.colorScheme.primary),
            topBar = {
                LibraryTopBar(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    isCollapsed: Boolean,
    todoViewModel: TodoViewModel,
    isEyeClosed: Boolean?,
    countCompletedTodo: Int?
) {
    val elevation = if (isCollapsed) 8.dp else 0.dp

    Column(
        modifier = Modifier
            .shadow(elevation)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        LargeTopAppBar(
            title = {
                if (scrollBehavior.state.collapsedFraction < 0.5) {
                    Text(
                        text = stringResource(id = R.string.my_todo),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 48.dp)
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.my_todo),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = if (isCollapsed) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
            ),
            actions = {
                if (scrollBehavior.state.collapsedFraction > 0.8) {
                    Image(
                        painter = painterResource(
                            if (isEyeClosed == true) R.drawable.visibility_off
                            else R.drawable.visibility
                        ),
                        contentDescription = if (isEyeClosed == true) stringResource(id = R.string.visibility) else stringResource(id = R.string.visibility_off),
                        modifier = Modifier
                            .clickable {
                                todoViewModel.isEyeClosed.value =
                                    todoViewModel.isEyeClosed.value?.let { !it } ?: false
                            }
                            .padding(horizontal = 20.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )

        AnimatedVisibility(
            visible = scrollBehavior.state.collapsedFraction < 0.5,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 64.dp,
                        end = 24.dp,
                        bottom = 10.dp
                    )
                    .background(MaterialTheme.colorScheme.primary),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Выполнено - ${countCompletedTodo ?: 0}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Image(
                    painter = painterResource(
                        if (isEyeClosed == true) R.drawable.visibility_off
                        else R.drawable.visibility
                    ),
                    contentDescription = if (isEyeClosed == true) stringResource(id = R.string.visibility) else stringResource(id = R.string.visibility_off),
                    modifier = Modifier
                        .clickable {
                            todoViewModel.isEyeClosed.value =
                                todoViewModel.isEyeClosed.value?.let { !it } ?: false
                        },
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Light Theme")
@Composable
fun HomeScreenPreviewLight() {
    val fakeTodoItemsRepository = object : ITodoItemsRepository {
        override val allTodo: Flow<List<TodoItem>> = flowOf(
            listOf(
                TodoItem(
                    id = "2",
                    text = "Read a book",
                    importance = Importance.LOW,
                    deadline = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null
                ),
                TodoItem(
                    id = "3",
                    text = "Read a book",
                    importance = Importance.HIGH,
                    deadline = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null
                ),
                TodoItem(
                    id = "4",
                    text = "Read a book",
                    importance = Importance.NO,
                    deadline = Date(System.currentTimeMillis()+ 172800000),
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null
                )
            )
        )
        override val incompleteTodo: Flow<List<TodoItem>> = allTodo.map { todoList ->
            todoList.filter { !it.isCompleted }
        }
        override suspend fun insert(todo: TodoItem) {}
        override suspend fun deleteTodoById(id: String) {}
        override suspend fun toggleCompletedById(id: String) {}
        override fun getTodoById(id: String): Flow<TodoItem?> = flowOf(null)
        override fun getCompletedTodoCount(): Flow<Int> = flowOf(1)
    }

    val fakeTodoViewModel = TodoViewModel(fakeTodoItemsRepository)

    val navController = rememberNavController()

    TodoYaTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(todoViewModel = fakeTodoViewModel, navController = navController)
        }
    }
}


@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun HomeScreenPreviewDark() {
    val fakeTodoItemsRepository = object : ITodoItemsRepository {
        override val allTodo: Flow<List<TodoItem>> = flowOf(
            listOf(
                TodoItem(
                    id = "2",
                    text = "Read a book",
                    importance = Importance.LOW,
                    deadline = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null
                ),
                TodoItem(
                    id = "3",
                    text = "Read a book",
                    importance = Importance.HIGH,
                    deadline = null,
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null
                ),
                TodoItem(
                    id = "4",
                    text = "Read a book",
                    importance = Importance.NO,
                    deadline = Date(System.currentTimeMillis()+ 172800000),
                    isCompleted = false,
                    createdAt = Date(),
                    modifiedAt = null
                )
            )
        )
        override val incompleteTodo: Flow<List<TodoItem>> = allTodo.map { todoList ->
            todoList.filter { !it.isCompleted }
        }
        override suspend fun insert(todo: TodoItem) {}
        override suspend fun deleteTodoById(id: String) {}
        override suspend fun toggleCompletedById(id: String) {}
        override fun getTodoById(id: String): Flow<TodoItem?> = flowOf(null)
        override fun getCompletedTodoCount(): Flow<Int> = flowOf(1)
    }

    val fakeTodoViewModel = TodoViewModel(fakeTodoItemsRepository)

    val navController = rememberNavController()

    TodoYaTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HomeScreen(todoViewModel = fakeTodoViewModel, navController = navController)
        }
    }
}








