package com.kalleerikssoon.snustracker.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
/**
 * Utility class responsible for managing location-related tasks in the app.
 * handles location permission checks, requests, and retrieves the current location using the
 * FusedLocationProviderClient. Communicates with the Android system to ensure that location
 * services are enabled and retrieves the user's location data.
 *
 * @property activity activity context required to access system services and request permissions.
 */
class LocationHandler(private val activity: Activity) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    // Function to check if location permission is granted
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    // Function to request location permissions from the user
    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    // Function to retrieve the current location if permission is granted
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Double, Double) -> Unit) {
        if (hasLocationPermission()) {
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("LocationHandler", "Location obtained: ${location.latitude}, ${location.longitude}")
                    onLocationReceived(location.latitude, location.longitude)
                } else {
                    Log.d("LocationHandler", "Location is null. Using default values.")
                    onLocationReceived(0.0, 0.0)
                }
            }.addOnFailureListener { exception ->
                Log.e("LocationHandler", "Error obtaining location: ${exception.message}")
                onLocationReceived(0.0, 0.0)
            }
        } else {
            onLocationReceived(0.0, 0.0)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

