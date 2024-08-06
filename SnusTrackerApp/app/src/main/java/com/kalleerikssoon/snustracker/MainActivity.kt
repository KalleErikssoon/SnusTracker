package com.kalleerikssoon.snustracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kalleerikssoon.snustracker.ui.theme.SnusTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnusTrackerTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var nrOfSnusToday = 0

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
                        if (nrOfSnusToday > 0) nrOfSnusToday--
                        coroutineScope.launch {
                            Toast.makeText(context, "Remove Snus Clicked", Toast.LENGTH_SHORT).show()
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
                        nrOfSnusToday++
                        coroutineScope.launch {
                            Toast.makeText(context, "Add Snus Clicked", Toast.LENGTH_SHORT).show()
                        }
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
                    text = "Amount of snus today: $nrOfSnusToday",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 25.dp)

                )
                val imageSize = 300.dp // Adjust this value as needed
                Image(
                    painter = painterResource(id = R.drawable.logo_no_background), // Replace with your drawable resource name
                    contentDescription = "Snus Lid",
                    modifier = Modifier.size(imageSize),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SnusTrackerTheme {
        MainScreen()
    }
}
