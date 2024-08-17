package com.kalleerikssoon.snustracker.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.kalleerikssoon.snustracker.utils.Screen
import com.kalleerikssoon.snustracker.viewmodels.SnusViewModel
import com.kalleerikssoon.snustracker.utils.UserSettings
import com.kalleerikssoon.snustracker.ui.components.BottomNavigationBar
import com.kalleerikssoon.snustracker.ui.components.EditCostDialog
import com.kalleerikssoon.snustracker.ui.components.EditHomeScreenDialog
import com.kalleerikssoon.snustracker.ui.components.EditPortionDialog
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.platform.LocalContext
import android.provider.Settings

/**
 * A composable for the settings screen of the app. The screen allows users to change
 * settings such as portions per package, cost per package, home screen time period displayed, and dark mode preferences.
 * It also opens the system's location service settings. Dialogs are used to change specific
 * settings.
 *
 * @param viewModel SnusViewModel that provides data and functions for the Settings screen.
 * @param navController NavHostController used for navigation between screens.
 */
@Composable
fun SettingsScreen(viewModel: SnusViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val userSettingsHelper = UserSettings.getInstance()

    var showEditPortionsDialog by remember { mutableStateOf(false) }
    var showEditCostDialog by remember { mutableStateOf(false) }
    var showEditHomeScreenDialog by remember { mutableStateOf(false) }


    var selectedPeriod by remember { mutableStateOf(userSettingsHelper.homeScreenPeriod) }

    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, currentScreen = Screen.Settings) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingsItem(
                title = "Portions Per Package",
                description = "${viewModel.portionsPerPackage.observeAsState(20).value} portions",
                control = {
                    Button(onClick = { showEditPortionsDialog = true }) {
                        Text("Edit")
                    }
                }
            )

            SettingsItem(
                title = "Cost Per Package",
                description = "${viewModel.costPerPackage.observeAsState(30).value} kr",
                control = {
                    Button(onClick = { showEditCostDialog = true }) {
                        Text("Edit")
                    }
                }
            )

            SettingsItem(
                title = "Home Screen Time Period",
                description = selectedPeriod,
                control = {
                    Button(onClick = { showEditHomeScreenDialog = true }) {
                        Text("Edit")
                    }
                }
            )

            SettingsItem(
                title = "Change Location Service Settings",
                description = "Location Services",
                control = {
                    Button(onClick = {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        context.startActivity(intent)
                    }) {
                        Text("Edit")
                    }
                }
            )

            val darkModeEnabled by viewModel.darkModeEnabled.observeAsState(false)
            SettingsItem(
                title = "Dark Mode",
                description = if (darkModeEnabled) "Enabled" else "Disabled",
                control = {
                    Switch(
                        checked = darkModeEnabled,
                        onCheckedChange = {
                            viewModel.updateDarkModeEnabled(it)
                        }
                    )
                }
            )
        }

        if (showEditPortionsDialog) {
            EditPortionDialog(
                currentPortions = viewModel.portionsPerPackage.observeAsState(20).value.toInt(),
                onSaveClick = { newPortions ->
                    viewModel.updatePortionsPerPackage(newPortions)
                    showEditPortionsDialog = false
                },
                onDismissRequest = { showEditPortionsDialog = false }
            )
        }

        if (showEditCostDialog) {
            EditCostDialog(
                currentCost = viewModel.costPerPackage.observeAsState(30).value.toInt(),
                onSaveClick = { newCost ->
                    viewModel.updateCostPerPackage(newCost)
                    showEditCostDialog = false
                },
                onDismissRequest = { showEditCostDialog = false }
            )
        }

        if (showEditHomeScreenDialog) {
            EditHomeScreenDialog(
                currentPeriod = selectedPeriod,
                onPeriodSelected = { newPeriod ->
                    selectedPeriod = newPeriod
                    userSettingsHelper.homeScreenPeriod = newPeriod
                    showEditHomeScreenDialog = false
                },
                onDismiss = { showEditHomeScreenDialog = false }
            )
        }
    }
}


@Composable
fun SettingsItem(
    title: String,
    description: String,
    control: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        control()
    }
}
