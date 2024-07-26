package presentation.settingsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        settingsRepository.themePreferenceFlow
            .onEach { theme ->
                _uiState.value = _uiState.value.copy(themeMode = theme)
                Log.d("Proslsush", "ViewModeUIState")
            }
            .launchIn(viewModelScope)
    }

    fun setTheme(theme: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
            Log.d("Proslsush", "ViewModelSet")
        }
    }
}