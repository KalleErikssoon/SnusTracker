// MainScreen.kt
package com.kalleerikssoon.snustracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kalleerikssoon.snustracker.R
import com.kalleerikssoon.snustracker.Screen
import com.kalleerikssoon.snustracker.SnusViewModel
import com.kalleerikssoon.snustracker.database.SnusEntry
import com.kalleerikssoon.snustracker.ui.components.BottomNavigationBar
import com.kalleerikssoon.snustracker.ui.components.HomeScreenAppBar

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kalleerikssoon.snustracker.TimePeriod
import com.kalleerikssoon.snustracker.UserSettings
import com.kalleerikssoon.snustracker.ui.components.EditHomeScreenDialog
import com.kalleerikssoon.snustracker.ui.components.InfoDialog

@Composable
fun HomeScreen(viewModel: SnusViewModel, navController: NavHostController) {
    val userSettingsHelper = UserSettings.getInstance()
    val todayEntries by viewModel.todayEntries.observeAsState(emptyList())
    val showInfoDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }

    // Load the saved period from SharedPreferences
    var selectedPeriod by remember { mutableStateOf(userSettingsHelper.homeScreenPeriod) }

    val displayedEntries = when (selectedPeriod) {
        "Daily" -> todayEntries.size
        TimePeriod.Weekly.name -> viewModel.getEntriesForWeek().observeAsState(emptyList()).value.size
        TimePeriod.Monthly.name -> viewModel.getEntriesForMonth().observeAsState(emptyList()).value.size
        TimePeriod.Yearly.name -> viewModel.getEntriesForYear().observeAsState(emptyList()).value.size
        TimePeriod.Total.name -> viewModel.getAllEntries().observeAsState(emptyList()).value.size
        else -> todayEntries.size
    }

    if (showEditDialog.value) {
        EditHomeScreenDialog(
            currentPeriod = selectedPeriod,
            onDismiss = { showEditDialog.value = false },
            onPeriodSelected = { period ->
                selectedPeriod = period
                userSettingsHelper.homeScreenPeriod = period // Save the selected period to SharedPreferences
                showEditDialog.value = false
            }
        )
    }

    Scaffold(
        topBar = {
            HomeScreenAppBar(
                onInfoClick = { showInfoDialog.value = true }, // Trigger the dialog
                onEditClick = { showEditDialog.value = true }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController, currentScreen = Screen.Home) },
        floatingActionButton = {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(25.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        if (todayEntries.isNotEmpty()) {
                            val latestEntry = todayEntries[0]
                            viewModel.delete(latestEntry)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Snus"
                    )
                }
                FloatingActionButton(
                    onClick = {
                        val currentTime = System.currentTimeMillis()
                        val newEntry = SnusEntry(
                            id = 0,
                            timestamp = currentTime,
                            latitude = 0.0,
                            longitude = 0.0
                        )
                        viewModel.insertSnusEntryWithLocation(newEntry)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Snus"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Amount of snus ${selectedPeriod}: $displayedEntries",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 25.dp)
                )
                val imageSize = 200.dp
                Image(
                    painter = painterResource(id = R.drawable.logo_no_background),
                    contentDescription = "Snus Lid",
                    modifier = Modifier.size(imageSize),
                )
            }
        }

        // Show the InfoDialog if triggered
        InfoDialog(showDialog = showInfoDialog)
    }
}