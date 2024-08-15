package com.kalleerikssoon.snustracker.ui.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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

@Composable
fun MapScreen(viewModel: SnusViewModel, navController: NavHostController) {
    val cameraPositionState = rememberCameraPositionState()

    val currentLocation by viewModel.currentLocation.observeAsState()

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

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, currentScreen = Screen.MapView) }
    ) { paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState
        ) {
            val locations = viewModel.getAllEntries().observeAsState(emptyList()).value

            locations.forEach { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "Snus Usage",
                    snippet = "Used snus at this location"
                )
            }
        }
    }
}