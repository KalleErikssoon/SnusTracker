package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kalleerikssoon.snustracker.Screen
import com.kalleerikssoon.snustracker.SnusViewModel
import com.kalleerikssoon.snustracker.ui.screens.HomeScreen
import com.kalleerikssoon.snustracker.ui.screens.MapScreen
import com.kalleerikssoon.snustracker.ui.screens.SettingsScreen
import com.kalleerikssoon.snustracker.ui.screens.StatsScreen

@Composable
fun SetupNavGraph(navController: NavHostController, viewModel: SnusViewModel) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(viewModel, navController) }
        composable(Screen.MapView.route) { MapScreen(viewModel, navController) }
        composable(Screen.Statistics.route) { StatsScreen(viewModel, navController) }
        composable(Screen.Settings.route) { SettingsScreen(viewModel, navController) }
    }
}
