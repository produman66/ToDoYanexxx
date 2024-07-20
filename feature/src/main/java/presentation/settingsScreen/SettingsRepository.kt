package presentation.settingsScreen

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_key")
    }

    val themePreferenceFlow: Flow<ThemeMode> = dataStore.data
    .map { preferences ->
        when (preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name) {
            ThemeMode.LIGHT.name -> ThemeMode.LIGHT
            ThemeMode.DARK.name -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    suspend fun setTheme(theme: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
            Log.d("Proslsush", "RepositorySet")
        }
    }
}