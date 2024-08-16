package com.kalleerikssoon.snustracker.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.kalleerikssoon.snustracker.SnusViewModel
import com.google.maps.android.compose.*
import com.kalleerikssoon.snustracker.Screen
import com.kalleerikssoon.snustracker.ui.components.BottomNavigationBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.compose.clustering.Clustering
import com.kalleerikssoon.snustracker.SnusClusterItem

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(viewModel: SnusViewModel, navController: NavHostController) {
    val cameraPositionState = rememberCameraPositionState()

    val currentLocation by viewModel.currentLocation.observeAsState()
    val snusEntries = viewModel.getAllEntries().observeAsState(emptyList()).value

    val snusMarkers = remember(snusEntries) {
        snusEntries.map { entry ->
            // give small offset to show all entries (if they have exact same long/lat values)
            val offset = 0.001
            val adjustedLatitude = entry.latitude + (Math.random() - 0.5) * offset
            val adjustedLongitude = entry.longitude + (Math.random() - 0.5) * offset

            // Format the timestamp to a readable date/time string
            val dateTime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(entry.timestamp)

            SnusClusterItem(
                itemPosition = LatLng(adjustedLatitude, adjustedLongitude),
                itemTitle = "Snus Entry Date & Time",
                itemSnippet = dateTime
            )
        }
    }

    // Update the camera position when currentLocation is updated
    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    // Fetch the user's location
    LaunchedEffect(Unit) {
        viewModel.fetchCurrentLocation()
    }

    // State to manage the dialog
    var showClusterDialog by remember { mutableStateOf(false) }
    var clusterInfo by remember { mutableStateOf<Cluster<SnusClusterItem>?>(null) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, currentScreen = Screen.MapView) }
    ) { paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState
        ) {
            Clustering(
                items = snusMarkers,
                onClusterClick = { cluster ->
                    clusterInfo = cluster
                    showClusterDialog = true
                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngZoom(
                            cluster.position,
                            cameraPositionState.position.zoom + 1
                        )
                    )
                    true
                },
                onClusterItemClick = {
                    false
                }
            )
        }
    }

    // Composable to display the cluster information
    if (showClusterDialog && clusterInfo != null) {
        val snusCount = clusterInfo!!.size
        val firstUsage = clusterInfo!!.items.minByOrNull { it.itemSnippet }?.itemSnippet ?: ""
        val lastUsage = clusterInfo!!.items.maxByOrNull { it.itemSnippet }?.itemSnippet ?: ""

        ClusterInfoWindow(
            clusterSize = snusCount,
            firstUsage = firstUsage,
            lastUsage = lastUsage,
            onDismiss = { showClusterDialog = false }
        )
    }
}

@Composable
fun ClusterInfoWindow(clusterSize: Int, firstUsage: String, lastUsage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Cluster Information") },
        text = {
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Cluster contains: ")
                        }
                        append("$clusterSize entries.")
                    }
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("First entry: ")
                        }
                        append(firstUsage)
                    }
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Last entry: ")
                        }
                        append(lastUsage)
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

