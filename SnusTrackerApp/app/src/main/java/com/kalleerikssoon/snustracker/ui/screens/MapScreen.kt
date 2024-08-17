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
import com.kalleerikssoon.snustracker.viewmodels.SnusViewModel
import com.google.maps.android.compose.*
import com.kalleerikssoon.snustracker.utils.Screen
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
import com.kalleerikssoon.snustracker.utils.SnusClusterItem
import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
/**
 * A composable for the map screen of the app. This screen displays a map with snus
 * entries clustered based on their geographical proximity. The user's current location is obtained and used to
 * center the map, if location permissions are granted. If the location permission is denied, a dialog
 * prompts the user to enable it in the system settings. When a cluster is clicked, detailed information about the
 * cluster's snus entries is displayed in a dialog.
 *
 * @param viewModel The SnusViewModel that provides data and functions for the Map screen.
 * @param navController The NavHostController used for navigation between screens.
 */
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(viewModel: SnusViewModel, navController: NavHostController) {

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val currentLocation by viewModel.currentLocation.observeAsState()
    val snusEntries = viewModel.getAllEntries().observeAsState(emptyList()).value
    var showPermissionDialog by remember { mutableStateOf(false) }

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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.fetchCurrentLocation()
        } else {
            showPermissionDialog = true
        }
    }

    // Check location permission on launch
    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED -> {
                viewModel.fetchCurrentLocation()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    // Update the camera position when currentLocation is updated
    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    // Show dialog if permission is not granted
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Location Permission Required") },
            text = { Text("This screen needs location permission to get your current location and display snus entries") },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                    showPermissionDialog = false
                }) {
                    Text("Go to Settings")
                }
            },
            dismissButton = {
                Button(onClick = { showPermissionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentLocation()
    }

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

