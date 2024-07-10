package com.example.todoya.presentation.ui.editTodoScreen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todoya.R
import com.example.todoya.Utils
import com.example.todoya.Utils.showErrorSnackbar
import com.example.todoya.data.room.entity.Importance
import com.example.todoya.data.room.entity.TodoItem
import com.example.todoya.presentation.common.CustomDivider
import com.example.todoya.ui.theme.TodoYaTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID


/**
 * Screen for editing a todo item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(
    uiState: EditTodoUiState,
    onGetTodoById: (String) -> Unit,
    onInsertTodo: (TodoItem) -> Unit,
    onDeleteTodoById: (String) -> Unit,
    onClearError: () -> Unit,
    onSyncWithServer: () -> Unit,
    navController: NavController,
    itemId: String
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var expanded by remember { mutableStateOf(false) }
    val dateDialogState = rememberMaterialDialogState()

    LaunchedEffect(itemId) {
        onGetTodoById(itemId)
    }


    var text by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(uiState.selectedTodoItem?.deadline != null) }
    var deadlineIsSelected by remember { mutableStateOf(uiState.selectedTodoItem?.deadline != null) }
    var pickedDate by remember { mutableStateOf(uiState.selectedTodoItem?.deadline) }
    var selectedOption by remember { mutableStateOf("") }


    uiState.errorCode?.let {
        LaunchedEffect(it) {
            showErrorSnackbar(it, snackbarHostState, scope, onSyncWithServer)
            onClearError()
        }
    }

    LaunchedEffect(uiState.selectedTodoItem) {
        text = uiState.selectedTodoItem?.text ?: ""
        selectedOption = when (uiState.selectedTodoItem?.importance) {
            Importance.LOW -> "Низкий"
            Importance.HIGH -> "Высокий"
            else -> "Нет"
        }
        isChecked = uiState.selectedTodoItem?.deadline != null
        deadlineIsSelected = uiState.selectedTodoItem?.deadline != null
        pickedDate = uiState.selectedTodoItem?.deadline
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary),

        topBar = {
            EditTopBar(
                onSaveClick = {
                    saveTodoItem(text, uiState.selectedTodoItem, selectedOption, pickedDate, onInsertTodo, navController, scope, snackbarHostState)
                },
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.primary)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.do_something),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                textStyle = MaterialTheme.typography.titleMedium,
                shape = RoundedCornerShape(12.dp),
                maxLines = Int.MAX_VALUE,
                minLines = 5,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    disabledBorderColor = MaterialTheme.colorScheme.secondary,
                    errorBorderColor = MaterialTheme.colorScheme.secondary
                )
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                text = stringResource(id = R.string.importance),
                style = MaterialTheme.typography.titleMedium,
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .clickable { expanded = true }
                        .align(Alignment.CenterStart),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = "Нет"
                            expanded = false
                        },
                        text = { Text(stringResource(id = R.string.NO)) }
                    )
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = "Низкий"
                            expanded = false
                        },
                        text = { Text(stringResource(id = R.string.LOW)) }
                    )
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = "Высокий"
                            expanded = false
                        },
                        text = {
                            Text(
                                stringResource(id = R.string.HIGH),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }

            CustomDivider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        text = stringResource(id = R.string.deadline),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    if (deadlineIsSelected && isChecked) {
                        Text(
                            text = SimpleDateFormat(
                                "d MMMM yyyy",
                                Locale("ru", "RU")
                            ).format(pickedDate!!),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable {
                                    dateDialogState.show()
                                }
                        )
                    }


                }
                Switch(
                    modifier = Modifier.padding(end = 16.dp),
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = !isChecked
                        if (isChecked) {
                            dateDialogState.show()
                            deadlineIsSelected = false
                        } else {
                            deadlineIsSelected = false
                            pickedDate = null
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedBorderColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedBorderColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }



            CustomDivider(modifier = Modifier.padding(vertical = 16.dp))

            DeleteButtonRow(itemId, onDeleteTodoById, navController)
        }


        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(
                    text = stringResource(id = R.string.ok),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.tertiary)
                ) {
                    deadlineIsSelected = true
                    isChecked = true
                }
                negativeButton(
                    text = stringResource(id = R.string.cancel),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.tertiary)
                ) {
                    if (!deadlineIsSelected) {
                        isChecked = false
                    }
                }
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = DateTimeFormatter
                    .ofPattern(
                        "d MMMM yyyy",
                        Locale("ru", "RU")
                    )
                    .format(LocalDate.now()),
                locale = Locale("ru", "RU"),
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colorScheme.tertiary,
                    dateActiveBackgroundColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                val date = Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
                pickedDate = date
                isChecked = true
            }
        }
    }
}


/**
 * Saves the todo item based on provided data.
 */
fun saveTodoItem(
    text: String,
    todoItem: TodoItem?,
    selectedOption: String,
    pickedDate: Date?,
    onInsertTodo: (TodoItem) -> Unit,
    navController: NavController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    if (text.trim().isNotEmpty()) {
        if (todoItem != null) {
            onInsertTodo(
                todoItem.copy(
                    text = text,
                    importance = Utils.mapSelectedOptionToImportance(selectedOption),
                    deadline = pickedDate,
                    modifiedAt = Date(System.currentTimeMillis()),
                    isSynced = true,
                    isModified = false
                )
            )
        } else {
            onInsertTodo(
                TodoItem(
                    id = UUID.randomUUID().toString(),
                    text = text,
                    importance = Utils.mapSelectedOptionToImportance(selectedOption),
                    deadline = pickedDate,
                    isCompleted = false,
                    createdAt = Date(System.currentTimeMillis()),
                    modifiedAt = Date(System.currentTimeMillis()),
                    isSynced = false,
                    isModified = false,
                    isDeleted = false
                )
            )
        }
        navController.popBackStack()
    } else {
        scope.launch {
            snackbarHostState.showSnackbar("Напиши хоть что-то")
        }
    }
}


/**
 * Row with delete button for deleting a todo item.
 */
@Composable
fun DeleteButtonRow(
    itemId: String,
    onDeleteTodoById: (String) -> Unit,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .let {
                if (itemId.isNotBlank()) {
                    it.clickable {
                        onDeleteTodoById(itemId)
                        navController.popBackStack()
                    }
                } else {
                    it
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTintColor =
            if (itemId.isNotBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant
        val textColor =
            if (itemId.isNotBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant

        Icon(
            painter = painterResource(id = R.drawable.delete),
            contentDescription = stringResource(id = R.string.delete),
            tint = iconTintColor
        )

        Text(
            text = stringResource(id = R.string.delete),
            modifier = Modifier
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}



/**
 * Preview of the edit todo screen in light mode.
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun EditTodoScreenPreviewLight() {
    TodoYaTheme(darkTheme = false) {
        val fakeUiState = EditTodoUiState(
            selectedTodoItem = TodoItem(
                id = "1",
                text = "Sample Todo",
                importance = Importance.LOW,
                deadline = Date(),
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = Date(),
                isSynced = false,
                isModified = false,
                isDeleted = false
            ),
            errorCode = null
        )

        val fakeOnGetTodoById: (String) -> Unit = {}
        val fakeOnInsertTodo: (TodoItem) -> Unit = {}
        val fakeOnDeleteTodoById: (String) -> Unit = {}
        val fakeOnClearError: () -> Unit = {}
        val fakeOnSyncWithServer: () -> Unit = {} // Добавляем фиктивную функцию для синхронизации

        val navController = rememberNavController()

        EditTodoScreen(
            uiState = fakeUiState,
            onGetTodoById = fakeOnGetTodoById,
            onInsertTodo = fakeOnInsertTodo,
            onDeleteTodoById = fakeOnDeleteTodoById,
            onClearError = fakeOnClearError,
            onSyncWithServer = fakeOnSyncWithServer, // Передаем фиктивную функцию синхронизации
            navController = navController,
            itemId = "1"
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditTodoScreenPreviewDark() {
    TodoYaTheme(darkTheme = true) {
        val fakeUiState = EditTodoUiState(
            selectedTodoItem = TodoItem(
                id = "1",
                text = "Sample Todo",
                importance = Importance.LOW,
                deadline = Date(),
                isCompleted = false,
                createdAt = Date(),
                modifiedAt = Date(),
                isSynced = false,
                isModified = false,
                isDeleted = false
            ),
            errorCode = null
        )

        val fakeOnGetTodoById: (String) -> Unit = {}
        val fakeOnInsertTodo: (TodoItem) -> Unit = {}
        val fakeOnDeleteTodoById: (String) -> Unit = {}
        val fakeOnClearError: () -> Unit = {}
        val fakeOnSyncWithServer: () -> Unit = {} // Добавляем фиктивную функцию для синхронизации

        val navController = rememberNavController()

        EditTodoScreen(
            uiState = fakeUiState,
            onGetTodoById = fakeOnGetTodoById,
            onInsertTodo = fakeOnInsertTodo,
            onDeleteTodoById = fakeOnDeleteTodoById,
            onClearError = fakeOnClearError,
            onSyncWithServer = fakeOnSyncWithServer,
            navController = navController,
            itemId = "1"
        )
    }
}






