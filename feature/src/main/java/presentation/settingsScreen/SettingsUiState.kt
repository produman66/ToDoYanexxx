package presentation.settingsScreen

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}


