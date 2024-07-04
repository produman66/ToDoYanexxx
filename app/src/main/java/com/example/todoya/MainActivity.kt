package com.example.todoya


import android.content.pm.ActivityInfo
import android.os.Bundle
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
import com.example.todoya.presentation.ui.ConnectivityObserver
import com.example.todoya.presentation.ui.NetworkConnectivityObserver
import com.example.todoya.ui.theme.TodoYaTheme
import com.example.todoya.presentation.viewmodel.TodoViewModel
import com.example.todoya.presentation.viewmodel.TodoViewModelFactory

class MainActivity : ComponentActivity() {

    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((application as TodoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()
        setContent {
            TodoYaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ){
                    val navController: NavHostController = rememberNavController()
                    TodoNavHost(todoViewModel = todoViewModel, navController = navController)
                }
            }
        }
    }
}
