package com.example.todoya.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.todoya.presentation.ui.EditTodoScreen
import com.example.todoya.presentation.ui.HomeScreen
import com.example.todoya.presentation.viewmodel.TodoViewModel



@Composable
fun TodoNavHost(
    navController: NavHostController,
    todoViewModel: TodoViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = "appScreen",
    ){
        navigation(
            route = "appScreen",
            startDestination = MainDestinations.HOME_LIST
        ) {
            composable(MainDestinations.ITEM_SCREEN + "/{itemId}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                EditTodoScreen(
                    todoViewModel = todoViewModel,
                    navController = navController,
                    itemId = itemId
                )
            }
            composable(MainDestinations.HOME_LIST) {
               HomeScreen(todoViewModel = todoViewModel, navController = navController)
            }
        }
    }
}

object MainDestinations {
    const val HOME_LIST = "todoListScreen"
    const val ITEM_SCREEN = "editItemScreen"
}