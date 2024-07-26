package presentation.settingsScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.example.feature.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.choose_theme),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.semantics {
                            traversalIndex = 2f
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = stringResource(id = R.string.back),
                            modifier = Modifier.semantics {
                                traversalIndex = 1f
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
                    .shadow(8.dp)
                    .semantics { isTraversalGroup = true }
            )
        },
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(padding)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)
                .semantics { isTraversalGroup = true }
        ) {
            RadioButtonWithLabel(
                label = stringResource(id = R.string.light_theme),
                selected = uiState.themeMode == ThemeMode.LIGHT,
                onClick = { settingsViewModel.setTheme(ThemeMode.LIGHT) },
                traversalIndex = 3f
            )
            RadioButtonWithLabel(
                label = stringResource(id = R.string.dark_theme),
                selected = uiState.themeMode == ThemeMode.DARK,
                onClick = { settingsViewModel.setTheme(ThemeMode.DARK) },
                traversalIndex = 4f
            )
            RadioButtonWithLabel(
                label = stringResource(id = R.string.system_theme),
                selected = uiState.themeMode == ThemeMode.SYSTEM,
                onClick = { settingsViewModel.setTheme(ThemeMode.SYSTEM) },
                traversalIndex = 5f,
            )
        }
    }
}

@Composable
fun RadioButtonWithLabel(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    traversalIndex: Float
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .background(color = MaterialTheme.colorScheme.primary)
            .semantics {
                this.traversalIndex = traversalIndex
                stateDescription = if (selected) "Активна" else "Неактивна"
            }
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = if (selected) RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.tertiary) else RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
}
