package com.example.todoya.presentation.ui

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
import com.example.todoya.R
import com.example.todoya.view.Utils
import com.example.todoya.data.entity.Importance
import com.example.todoya.data.entity.TodoItem
import com.example.todoya.ui.theme.TodoYaTheme
import java.util.Date

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
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onCheckedChange(it)
            },
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
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
            Row (modifier = Modifier
                .padding(bottom = 4.dp)){
                if (item.importance == Importance.LOW) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_down),
                        contentDescription = stringResource(id = R.string.importance),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
                    )
                }
                else if (item.importance == Importance.HIGH){
                    Image(
                        painter = painterResource(id = R.drawable.importance_high),
                        contentDescription = stringResource(id = R.string.importance),
                    )
                }
                Text(
                    text = item.text,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isChecked) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.onPrimary,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                )
            }
            if (item.deadline != null) {
                Text(
                    text = item.deadline.let { Utils.formatDate(it) },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outlineVariant

                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.info_outline),
            contentDescription = "Todo Icon",
            modifier = Modifier
                .padding(top = 26.dp, end = 16.dp)
                .size(24.dp),

            )
    }
}


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
    modifiedAt = null
)

@Preview(showBackground = true)
@Composable
fun TodoItemScreenPreview() {
    TodoYaTheme {
        TodoItemScreen(item = exampleTodoItem, onCheckedChange = {}, onItemClick = {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TodoItemScreenPreviewDark() {
    TodoYaTheme {
        TodoItemScreen(item = exampleTodoItem, onCheckedChange = {}, onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TodoNewPreview() {
    TodoYaTheme {
        TodoNew(onItemClick = {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TodoNewPreviewDark() {
    TodoYaTheme {
        TodoNew(onItemClick = {})
    }
}