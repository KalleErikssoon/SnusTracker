// MainScreen.kt
package com.kalleerikssoon.snustracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kalleerikssoon.snustracker.MainActivity
import com.kalleerikssoon.snustracker.R
import com.kalleerikssoon.snustracker.SnusViewModel
import com.kalleerikssoon.snustracker.database.SnusEntry
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: SnusViewModel) {
    val context = LocalContext.current as MainActivity
    val coroutineScope = rememberCoroutineScope()

    val todayEntries by viewModel.todayEntries.observeAsState(emptyList())

    Scaffold(
        bottomBar = {
            BottomAppBar(
                tonalElevation = 10.dp
            ) {
                NavigationBar {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Map View"
                            )
                        },
                        label = { Text("Map View") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch {
                                Toast.makeText(context, "Map View Clicked", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Statistics"
                            )
                        },
                        label = { Text("Statistics") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch {
                                Toast.makeText(context, "Statistics Clicked", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings"
                            )
                        },
                        label = { Text("Settings") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch {
                                Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        },
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
                            coroutineScope.launch {
                                Toast.makeText(context, "Snus Removed", Toast.LENGTH_SHORT).show()
                            }
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
                    text = "Amount of snus today: ${todayEntries.size}",
                    style = MaterialTheme.typography.headlineSmall,
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
    }
}
