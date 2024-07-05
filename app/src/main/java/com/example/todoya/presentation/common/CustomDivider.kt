package com.example.todoya.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoya.ui.theme.TodoYaTheme

@Composable
fun CustomDivider(modifier: Modifier){
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Preview(showBackground = true)
@Composable
fun TodoItemScreenPreview() {
    TodoYaTheme {
        CustomDivider(modifier = Modifier.padding(vertical = 20.dp, horizontal = 26.dp))
    }
}