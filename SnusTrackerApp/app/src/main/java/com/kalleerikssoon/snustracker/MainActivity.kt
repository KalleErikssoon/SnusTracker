package com.kalleerikssoon.snustracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.kalleerikssoon.snustracker.ui.screens.HomeScreen
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
        setContent {
            SnusTrackerTheme {
                HomeScreen(viewModel) // Pass the viewModel to HomeScreen
            }
        }
    }
}


