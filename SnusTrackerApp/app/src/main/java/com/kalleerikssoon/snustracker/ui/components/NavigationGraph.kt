package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kalleerikssoon.snustracker.utils.Screen
import com.kalleerikssoon.snustracker.viewmodels.SnusViewModel
import com.kalleerikssoon.snustracker.ui.screens.HomeScreen
import com.kalleerikssoon.snustracker.ui.screens.MapScreen
import com.kalleerikssoon.snustracker.ui.screens.SettingsScreen
import com.kalleerikssoon.snustracker.ui.screens.StatsScreen

/**
 * A composable that sets up the navigation graph for the app, defining the different screens
 * and their corresponding routes. The navigation graph manages the navigation between screens using a NavHostController.
 *
 * @param navController NavHostController used to navigate between screens.
 * @param viewModel SnusViewModel instance that is shared across different screens.
 */
@Composable
fun SetupNavGraph(navController: NavHostController, viewModel: SnusViewModel) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(viewModel, navController) }
        composable(Screen.MapView.route) { MapScreen(viewModel, navController) }
        composable(Screen.Statistics.route) { StatsScreen(viewModel, navController) }
        composable(Screen.Settings.route) { SettingsScreen(viewModel, navController) }
    }
}
