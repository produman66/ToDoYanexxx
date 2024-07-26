package presentation.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import presentation.editTodoScreen.EditTodoScreen
import presentation.editTodoScreen.EditTodoViewModel
import presentation.homeScreen.HomeScreen
import presentation.homeScreen.HomeViewModel
import presentation.infoScreen.InfoScreen
import presentation.settingsScreen.SettingsScreen
import presentation.settingsScreen.SettingsViewModel


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
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        navigation(
            route = "appScreen",
            startDestination = MainDestinations.HOME_LIST
        ) {
            composable(
                route = "${MainDestinations.ITEM_SCREEN}/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            500, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(500, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            500, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(500, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                val editTodoViewModel: EditTodoViewModel = hiltViewModel()
                val uiStateEdit by editTodoViewModel.uiState.collectAsState()
                EditTodoScreen(
                    uiState = uiStateEdit,
                    onGetTodoById = { id -> editTodoViewModel.getTodoById(id) },
                    onInsertTodo = { todo -> editTodoViewModel.insert(todo) },
                    onDeleteTodoById = { id -> editTodoViewModel.deleteTodoById(id) },
                    onUndoTodoById = { id -> editTodoViewModel.undoTodoById(id) },
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
                    onSyncWithServer = { homeViewModel.syncWithServer() },
                    onSettingsClick = { navController.navigate(MainDestinations.SETTINGS_SCREEN) },
                    onInfoClick = { navController.navigate(MainDestinations.INFO_SCREEN) },
                    onDeleteTodoById = { homeViewModel.deleteTodoById(uiState.undoList?.id ?: "") },
                    onUndoTodoById = { homeViewModel.undoTodoById(uiState.undoList?.id ?: "") },
                )
            }

            composable(
                route = MainDestinations.SETTINGS_SCREEN,
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(500, easing = LinearEasing)
                    ) + fadeIn(animationSpec = tween(500, easing = LinearEasing))
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(500, easing = LinearEasing)
                    ) + fadeOut(animationSpec = tween(500, easing = LinearEasing))
                }
            ) {
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                val uiState by settingsViewModel.uiState.collectAsState()
                SettingsScreen(
                    uiState = uiState,
                    onBack = { navController.popBackStack() },
                    settingsViewModel = settingsViewModel
                )
            }

            composable(
                route = MainDestinations.INFO_SCREEN,
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(500, easing = LinearEasing)
                    ) + fadeIn(animationSpec = tween(500, easing = LinearEasing))
                },
                exitTransition = {
                    slideOutVertically(
                        targetOffsetY = { -it },
                        animationSpec = tween(500, easing = LinearEasing)
                    ) + fadeOut(animationSpec = tween(500, easing = LinearEasing))
                }
            ) {
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                val uiState by settingsViewModel.uiState.collectAsState()
                InfoScreen(
                    uiState = uiState,
                    navController = navController,
                )
            }
        }
    }
}


object MainDestinations {
    const val HOME_LIST = "todoListScreen"
    const val ITEM_SCREEN = "editItemScreen"
    const val SETTINGS_SCREEN = "settingsScreen"
    const val INFO_SCREEN = "infoScreen"
}