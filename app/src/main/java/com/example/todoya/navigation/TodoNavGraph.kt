package com.example.todoya.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.todoya.presentation.editTodoScreen.EditTodoScreen
import com.example.todoya.presentation.homeScreen.HomeScreen
import com.example.todoya.feature.presentation.viewmodel.EditTodoViewModel
import com.example.todoya.feature.presentation.viewmodel.HomeViewModel


/**
 * Composable function representing the navigation host for the Todo application.
 */
@Composable
fun TodoNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "appScreen",
    ){
        navigation(
            route = "appScreen",
            startDestination = MainDestinations.HOME_LIST
        ) {
            composable(
                route = "${MainDestinations.ITEM_SCREEN}/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                val editTodoViewModel: EditTodoViewModel = hiltViewModel()
                val uiStateEdit by editTodoViewModel.uiState.collectAsState()

                EditTodoScreen(
                    uiState = uiStateEdit,
                    onGetTodoById = { id -> editTodoViewModel.getTodoById(id) },
                    onInsertTodo = { todo -> editTodoViewModel.insert(todo) },
                    onDeleteTodoById = { id -> editTodoViewModel.deleteTodoById(id) },
                    onClearError = { editTodoViewModel.clearError() },
                    onSyncWithServer = { editTodoViewModel.syncWithServer() },
                    navController = navController,
                    itemId = itemId
                )
            }
            composable(MainDestinations.HOME_LIST) {

                val homeViewModel: HomeViewModel = hiltViewModel()
                val uiState by homeViewModel.uiState.collectAsState()
                HomeScreen(
                    uiState = uiState,
                    navController = navController,
                    onToggleCompletedById = { id -> homeViewModel.toggleCompletedById(id) },
                    onClearError = { homeViewModel.clearError() },
                    onInitializeConnectivityObserver = { context -> homeViewModel.initializeConnectivityObserver(context) },
                    onToggleEyeClosed = { homeViewModel.toggleEyeClosed() },
                    onSyncWithServer = { homeViewModel.syncWithServer() }
                )
            }
        }
    }
}



object MainDestinations {
    const val HOME_LIST = "todoListScreen"
    const val ITEM_SCREEN = "editItemScreen"
}