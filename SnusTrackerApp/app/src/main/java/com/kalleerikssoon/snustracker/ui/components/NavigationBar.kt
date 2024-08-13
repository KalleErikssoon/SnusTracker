package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.kalleerikssoon.snustracker.Screen

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentScreen: Screen
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentScreen is Screen.Home,
            onClick = { navController.navigate(Screen.Home.route) }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Map View") },
            label = { Text("Map") },
            selected = currentScreen is Screen.MapView,
            onClick = { navController.navigate(Screen.MapView.route) }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "Statistics") },
            label = { Text("Statistics") },
            selected = currentScreen is Screen.Statistics,
            onClick = { navController.navigate(Screen.Statistics.route) }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentScreen is Screen.Settings,
            onClick = { navController.navigate(Screen.Settings.route) }
        )
    }
}
