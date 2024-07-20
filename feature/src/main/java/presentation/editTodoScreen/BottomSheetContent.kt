package presentation.editTodoScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ui.CustomDivider


@Composable
fun BottomSheetContent(
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Выберете важность:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        Text(
            text = "Нет",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOptionSelected("Нет") }
                .padding(8.dp),
            textAlign = TextAlign.Center
        )

        CustomDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "Низкий",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOptionSelected("Низкий") }
                .padding(8.dp),
            textAlign = TextAlign.Center
        )

        CustomDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(
            text = "Высокий",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOptionSelected("Высокий") }
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}
