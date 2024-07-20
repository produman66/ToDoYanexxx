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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import com.example.feature.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onBack: () -> Unit,
    settingsViewModel: SettingsViewModel
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.choose_theme),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = stringResource(id = R.string.back)
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
        ) {
            RadioButtonWithLabel(
                label = stringResource(id = R.string.light_theme),
                selected = uiState.themeMode == ThemeMode.LIGHT,
                onClick = { settingsViewModel.setTheme(ThemeMode.LIGHT) }
            )
            RadioButtonWithLabel(
                label = stringResource(id = R.string.dark_theme),
                selected = uiState.themeMode == ThemeMode.DARK,
                onClick = { settingsViewModel.setTheme(ThemeMode.DARK) }
            )
            RadioButtonWithLabel(
                label = stringResource(id = R.string.system_theme),
                selected = uiState.themeMode == ThemeMode.SYSTEM,
                onClick = { settingsViewModel.setTheme(ThemeMode.SYSTEM) }
            )
        }
    }
}

@Composable
fun RadioButtonWithLabel(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
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
        Log.d("Proslsush", "Screen")
    }
}
