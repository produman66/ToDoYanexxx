package presentation.homeScreen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import Utils
import androidx.compose.foundation.focusable
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.example.feature.R
import data.local.model.Importance
import data.local.model.TodoItem
import theme.TodoYaTheme
import java.util.Date


/**
 * Composable function for displaying a single TodoItem in a list.
 */
@Composable
fun TodoItemScreen(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onItemClick: () -> Unit
) {
    var isChecked by remember { mutableStateOf(item.isCompleted) }

    DisposableEffect(item.isCompleted) {
        isChecked = item.isCompleted
        onDispose { }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .background(MaterialTheme.colorScheme.secondary)
            .semantics {
                contentDescription = "Задача: ${item.text}. " +
                        (if (item.deadline != null) "Срок: ${Utils.formatDate(item.deadline)}. " else "") +
                        (if (isChecked) "Выполнено." else "Не выполнено.")
                role = Role.Button
            }.focusable()
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onCheckedChange(it)
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp)
                .semantics {
                    contentDescription = "Отметить выполненным." +
                            (if (isChecked) "Состояние: выполнено." else "Состояние: не выполнено.")
                    role = Role.Checkbox
                }.focusable(),
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.scrim,
                uncheckedColor =
                if (item.importance == Importance.HIGH) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outlineVariant,
                checkmarkColor = MaterialTheme.colorScheme.primary,
                disabledCheckedColor = MaterialTheme.colorScheme.scrim,
                disabledUncheckedColor = if (item.importance == Importance.HIGH) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outlineVariant,
            )
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 22.dp, bottom = 22.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 4.dp)
            ) {
                if (item.importance == Importance.LOW) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = stringResource(id = R.string.importance_low),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.semantics {
                            contentDescription = "Низкая важность"
                        }
                    )
                } else if (item.importance == Importance.HIGH) {
                    Image(
                        painter = painterResource(id = R.drawable.importance_high),
                        contentDescription = stringResource(id = R.string.importance),
                        modifier = Modifier.semantics {
                            contentDescription = "Высокая важность"
                        }
                    )
                }
                Text(
                    text = item.text,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isChecked) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.onPrimary,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                    modifier = Modifier.semantics {
                        contentDescription = item.text
                    } .focusable()
                )
            }
            if (item.deadline != null) {
                Text(
                    text = item.deadline.let { Utils.formatDate(it) },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.semantics {
                        contentDescription = "Срок: ${Utils.formatDate(item.deadline)}"
                    } .focusable()
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.info_outline),
            contentDescription = "Информация о задаче",
            modifier = Modifier
                .padding(top = 26.dp, end = 16.dp)
                .size(24.dp)
                .semantics {
                    role = Role.Button
                }
                .focusable()
        )
    }
}



/**
 * Composable function for displaying a "New Todo" button.
 */
@Composable
fun TodoNew(
    onItemClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .background(MaterialTheme.colorScheme.secondary)
            .semantics {
                contentDescription = "Добавить задачу"
                role = Role.Button
            }
    ) {
        Text(
            text = stringResource(R.string.new_todo),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                )
                .padding(bottom = 22.dp, start = 56.dp, top = 10.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
            style = MaterialTheme.typography.titleMedium
        )
    }
}


val exampleTodoItem = TodoItem(
    id = "3",
    text = "Finish project report",
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


/**
 * Preview function for TodoItemScreen in light theme.
 */
@Preview(showBackground = true)
@Composable
fun TodoItemScreenPreview() {
    TodoYaTheme {
        TodoItemScreen(item = exampleTodoItem, onCheckedChange = {}, onItemClick = {})
    }
}


/**
 * Preview function for TodoItemScreen in dark theme.
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TodoItemScreenPreviewDark() {
    TodoYaTheme {
        TodoItemScreen(item = exampleTodoItem, onCheckedChange = {}, onItemClick = {})
    }
}


/**
 * Preview function for TodoNew in light theme.
 */
@Preview(showBackground = true)
@Composable
fun TodoNewPreview() {
    TodoYaTheme {
        TodoNew(onItemClick = {})
    }
}


/**
 * Preview function for TodoNew in dark theme.
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TodoNewPreviewDark() {
    TodoYaTheme {
        TodoNew(onItemClick = {})
    }
}