package com.example.todoya


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todoya.presentation.navigation.TodoNavHost
import com.example.todoya.data.sp.AuthManager
import com.example.todoya.presentation.viewmodel.TodoViewModel
import com.example.todoya.presentation.viewmodel.TodoViewModelFactory
import com.example.todoya.ui.theme.TodoYaTheme
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * MainActivity manages the main user interface of the application
 *  * and sets up navigation using Jetpack Compose.
 */
class MainActivity : ComponentActivity() {

    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((application as TodoApplication).repository)
    }

    private lateinit var sdk: YandexAuthSdk
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authManager = AuthManager(this)
        sdk = YandexAuthSdk.create(YandexAuthOptions(this))

        if (!authManager.isLoggedIn()) {
            startAuthFlow()
        } else {
            continueWithSavedToken()
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()

        setContent {
            TodoYaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val navController: NavHostController = rememberNavController()
                    TodoNavHost(todoViewModel = todoViewModel, navController = navController)
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
}