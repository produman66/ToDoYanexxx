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
import com.example.todoya.ui.theme.TodoYaTheme
import com.example.todoya.presentation.viewmodel.TodoViewModel
import com.example.todoya.presentation.viewmodel.TodoViewModelFactory

//class MainActivity : AppCompatActivity() {
//
//    private var _binding: ActivityMainBinding? = null
//    private val bindingAM get() = _binding!!
//
//    val repository: TodoItemsRepository
//        get() = (application as TodoApplication).repository
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        _binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(bindingAM.root)
//
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        _binding = null
//    }
//}

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
