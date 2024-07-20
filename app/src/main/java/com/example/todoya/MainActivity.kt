package com.example.todoya


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import presentation.navigation.TodoNavHost
import theme.TodoYaTheme
import data.auth.AuthManager
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import presentation.settingsScreen.SettingsRepository
import presentation.settingsScreen.SettingsViewModel
import presentation.settingsScreen.ThemeMode
import javax.inject.Inject


/**
 * MainActivity manages the main user interface of the application
 *  * and sets up navigation using Jetpack Compose.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sdk: YandexAuthSdk

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var settingsRepository: SettingsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!authManager.isLoggedIn()) {
            startAuthFlow()
        } else {
            continueWithSavedToken()
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()

        setContent {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            TodoYaTheme(darkTheme = when (uiState.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val navController: NavHostController = rememberNavController()
                    TodoNavHost(navController = navController)
                }
            }
        }
    }



    private fun startAuthFlow() {
        val launcher = registerForActivityResult(sdk.contract) { result ->
            handleResult(result)
        }

        val loginOptions = YandexAuthLoginOptions()
        launcher.launch(loginOptions)
    }


    private fun continueWithSavedToken() {
        authManager.getSavedToken()?.let { savedToken ->
            (application as TodoApplication).updateAuthToken(savedToken)
        }
    }


    private fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessAuth(result.token)
            is YandexAuthResult.Failure -> onProcessError(result.exception)
            YandexAuthResult.Cancelled -> onCancelled()
        }
    }


    private fun onSuccessAuth(token: YandexAuthToken) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    val tokenValue = token.value
                    (application as TodoApplication).updateAuthToken(tokenValue)
                    authManager.saveAuthToken(tokenValue)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onProcessError(e)
                }
            }
        }
    }


    private fun onProcessError(exception: Exception) {
        Log.d("onCancelled", "onProcessError: ${exception.message}")
    }


    private fun onCancelled() {
        Log.d("yan2", "onCancelled")
    }


//    override fun onDestroy() {
//        super.onDestroy()
//        themeJob.cancel()
//    }
//
//
//    private suspend fun applyThemeFromPreferences() {
//        settingsRepository.themePreferenceFlow.collect { theme ->
//            isDarkTheme = when (theme) {
//                ThemeMode.LIGHT -> {
//                    ThemeMode.LIGHT
//                }
//                ThemeMode.DARK -> {
//                    ThemeMode.DARK
//                }
//                ThemeMode.SYSTEM -> {
//                    ThemeMode.SYSTEM
//                }
//            }
//        }
//    }

}