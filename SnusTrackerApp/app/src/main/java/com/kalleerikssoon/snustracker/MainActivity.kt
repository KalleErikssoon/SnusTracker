package com.kalleerikssoon.snustracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.kalleerikssoon.snustracker.ui.components.SetupNavGraph
import com.kalleerikssoon.snustracker.ui.theme.SnusTrackerTheme
import com.kalleerikssoon.snustracker.utils.LocationHandler
import com.kalleerikssoon.snustracker.utils.UserSettings
import com.kalleerikssoon.snustracker.viewmodels.SnusViewModel
import com.kalleerikssoon.snustracker.viewmodels.SnusViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: SnusViewModel by lazy {
        ViewModelProvider(
            this,
            SnusViewModelFactory(application, LocationHandler(this))
        )[SnusViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserSettings.initialize(this)

        setContent {
            val darkModeEnabled by viewModel.darkModeEnabled.observeAsState(UserSettings.getInstance().darkModeOn)

            SnusTrackerTheme(darkTheme = darkModeEnabled) {
                val navController = rememberNavController()

                SetupNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
