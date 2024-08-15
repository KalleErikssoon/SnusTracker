package com.kalleerikssoon.snustracker.ui.screens

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
import com.kalleerikssoon.snustracker.Screen
import com.kalleerikssoon.snustracker.SnusViewModel
import com.kalleerikssoon.snustracker.UserSettings
import com.kalleerikssoon.snustracker.ui.components.BottomNavigationBar
import com.kalleerikssoon.snustracker.ui.components.EditCostDialog
import com.kalleerikssoon.snustracker.ui.components.EditHomeScreenDialog
import com.kalleerikssoon.snustracker.ui.components.EditPortionDialog

@Composable
fun SettingsScreen(viewModel: SnusViewModel, navController: NavHostController) {
    val userSettingsHelper = UserSettings.getInstance()

    // Manage dialog visibility states
    var showEditPortionsDialog by remember { mutableStateOf(false) }
    var showEditCostDialog by remember { mutableStateOf(false) }
    var showEditHomeScreenDialog by remember { mutableStateOf(false) }

    // Load the saved period from SharedPreferences
    var selectedPeriod by remember { mutableStateOf(userSettingsHelper.homeScreenPeriod) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, currentScreen = Screen.Settings) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Portions Per Package Setting
            SettingsItem(
                title = "Portions Per Package",
                description = "${viewModel.portionsPerPackage.observeAsState(20).value} portions",
                control = {
                    Button(onClick = { showEditPortionsDialog = true }) {
                        Text("Edit")
                    }
                }
            )

            // Cost Per Package Setting
            SettingsItem(
                title = "Cost Per Package",
                description = "${viewModel.costPerPackage.observeAsState(30).value} kr",
                control = {
                    Button(onClick = { showEditCostDialog = true }) {
                        Text("Edit")
                    }
                }
            )

            // Home Screen Time Period Setting
            SettingsItem(
                title = "Home Screen Time Period",
                description = selectedPeriod,
                control = {
                    Button(onClick = { showEditHomeScreenDialog = true }) {
                        Text("Edit")
                    }
                }
            )

            // Dark/Light Mode
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

        // Show Dialogs
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
                    userSettingsHelper.homeScreenPeriod = newPeriod // Save the selected period to SharedPreferences
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
