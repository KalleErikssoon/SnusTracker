package com.kalleerikssoon.snustracker

import SetupNavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.kalleerikssoon.snustracker.ui.theme.SnusTrackerTheme

class MainActivity : ComponentActivity() {

    private val viewModel: SnusViewModel by lazy {
        ViewModelProvider(
            this,
            SnusViewModelFactory(application, LocationHandler(this)) // Passing both application and LocationHandler
        )[SnusViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserSettings.initialize(this)
        setContent {
            SnusTrackerTheme {
                val navController = rememberNavController() // Initialize NavController

                SetupNavGraph(navController = navController, viewModel = viewModel) // Set up the NavGraph
            }
        }
    }
}
