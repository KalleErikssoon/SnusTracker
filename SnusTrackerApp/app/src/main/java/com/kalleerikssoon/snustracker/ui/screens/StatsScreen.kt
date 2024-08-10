package com.kalleerikssoon.snustracker.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kalleerikssoon.snustracker.Screen
import com.kalleerikssoon.snustracker.SnusViewModel
import com.kalleerikssoon.snustracker.ui.components.BottomNavigationBar

@Composable
fun StatsScreen(viewModel: SnusViewModel, navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, currentScreen = Screen.Statistics) }
    ) { paddingValues ->
        // You can use the paddingValues if you need to apply padding to your content
        Text(
            text = "Statistics Screen",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(paddingValues)
        )
    }
}