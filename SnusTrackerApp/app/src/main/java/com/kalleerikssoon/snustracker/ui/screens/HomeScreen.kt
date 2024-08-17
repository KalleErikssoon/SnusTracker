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
import com.kalleerikssoon.snustracker.utils.Screen
import com.kalleerikssoon.snustracker.viewmodels.SnusViewModel
import com.kalleerikssoon.snustracker.database.SnusEntry
import com.kalleerikssoon.snustracker.ui.components.BottomNavigationBar
import com.kalleerikssoon.snustracker.ui.components.HomeScreenAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import com.kalleerikssoon.snustracker.utils.TimePeriod
import com.kalleerikssoon.snustracker.ui.components.InfoDialog

/**
 * A composable for the home screen of the app. This screen shows the
 * amount of snus consumed during a selected time period, buttons for adding or deleting snus entries. The screen layout
 * adjusts based on the device's orientation, in portrait mode, the FABs
 * Action Buttons (FABs) are centered below the logo, in landscape mode, the FABs are positioned on either
 * side of the logo.
 *
 * @param viewModel SnusViewModel that provides data and functions for the home screen.
 * @param navController NavHostController used for navigation between screens.
 */
@Composable
fun HomeScreen(viewModel: SnusViewModel, navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val todayEntries by viewModel.todayEntries.observeAsState(emptyList())
    val showInfoDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }
    val selectedPeriod by remember { mutableStateOf("Daily") }

    // Define the displayed entries based on the selected period
    val displayedEntries = when (selectedPeriod) {
        "Daily" -> todayEntries.size
        TimePeriod.Weekly.name -> viewModel.getEntriesForWeek().observeAsState(emptyList()).value.size
        TimePeriod.Monthly.name -> viewModel.getEntriesForMonth().observeAsState(emptyList()).value.size
        TimePeriod.Yearly.name -> viewModel.getEntriesForYear().observeAsState(emptyList()).value.size
        TimePeriod.Total.name -> viewModel.getAllEntries().observeAsState(emptyList()).value.size
        else -> todayEntries.size
    }

    Scaffold(
        topBar = {
            HomeScreenAppBar(
                onInfoClick = { showInfoDialog.value = true },
                onEditClick = { showEditDialog.value = true }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController, currentScreen = Screen.Home) },
        floatingActionButton = {
            if (isLandscape) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 50.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.spacedBy(250.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            onClick = {
                                if (todayEntries.isNotEmpty()) {
                                    val latestEntry = todayEntries[0]
                                    viewModel.delete(latestEntry)
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Snus")
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
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Snus")
                        }
                    }
                }
            } else {
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
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Snus")
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
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Snus")
                    }
                }
            }
        },
        floatingActionButtonPosition = if (isLandscape) FabPosition.Center else FabPosition.Center
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

        InfoDialog(showDialog = showInfoDialog)
    }
}
