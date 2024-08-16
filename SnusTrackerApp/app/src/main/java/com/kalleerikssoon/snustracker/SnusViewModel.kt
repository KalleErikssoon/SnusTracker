package com.kalleerikssoon.snustracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kalleerikssoon.snustracker.database.SnusEntry
import com.kalleerikssoon.snustracker.database.SnusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SnusViewModel(
    application: Application,
    private val repository: SnusRepository,
    private val locationHandler: LocationHandler
) : AndroidViewModel(application) {
    private val settingsManager = UserSettings.getInstance()
    val todayEntries: LiveData<List<SnusEntry>> = repository.getEntriesForToday()

    val costPerPackage: LiveData<Float> = MutableLiveData(settingsManager.costPerPackage)
    val portionsPerPackage: LiveData<Float> = MutableLiveData(settingsManager.portionsPerPackage)
    val darkModeEnabled: MutableLiveData<Boolean> = MutableLiveData(settingsManager.darkModeOn)

    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng> = _currentLocation
    private val isLocationTrackingEnabled: MutableLiveData<Boolean> = MutableLiveData(false)

    // Function to toggle location tracking
    fun toggleLocationTracking(isEnabled: Boolean) {
        if (isEnabled) {
            if (locationHandler.hasLocationPermission()) {
                isLocationTrackingEnabled.value = true
            } else {
                locationHandler.requestLocationPermission()
            }
        } else {
            isLocationTrackingEnabled.value = false
        }
    }

    // Function to get snus entries for the current week
    fun getEntriesForWeek(): LiveData<List<SnusEntry>> = repository.getEntriesForWeek()

    // Function to get snus entries for the current month
    fun getEntriesForMonth(): LiveData<List<SnusEntry>> = repository.getEntriesForMonth()

    // Function to get snus entries for the current year
    fun getEntriesForYear(): LiveData<List<SnusEntry>> = repository.getEntriesForYear()
    fun getAllEntries(): LiveData<List<SnusEntry>> = repository.allEntries

    // Function to insert a snus entry into the database with location handling
    fun insertSnusEntryWithLocation(entry: SnusEntry) {
        if (locationHandler.hasLocationPermission()) {
            locationHandler.getCurrentLocation { latitude, longitude ->
                insert(entry.copy(latitude = latitude, longitude = longitude))
            }
        } else {
            locationHandler.requestLocationPermission()
            insert(entry) // Insert with default location if permission is not granted
        }
    }
    fun fetchCurrentLocation() {
        locationHandler.getCurrentLocation { latitude, longitude ->
            _currentLocation.postValue(LatLng(latitude, longitude))
        }
    }
    fun hasLocationPermission(): Boolean {
        return locationHandler.hasLocationPermission()
    }

    // Internal function to insert the entry into the database
    fun insert(entry: SnusEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
        withContext(Dispatchers.Main) {
            logCurrentEntries("After Insert")
        }
    }

    // Function to delete a snus entry from the database
    fun delete(entry: SnusEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entry)
        withContext(Dispatchers.Main) {
            logCurrentEntries("After Delete")
        }
    }

    // Function to log current entries in the database
    private fun logCurrentEntries(operation: String) {
        todayEntries.observeForever { entries ->
            println("$operation - Current Entries: $entries")
        }
    }
    fun getMonthlyAverage(entries: List<SnusEntry>): Pair<Boolean, Float> {
        if (entries.isEmpty()) return Pair(true, 0f)

        val firstEntryDate = entries.minOfOrNull { it.timestamp } ?: return Pair(true, 0f)
        val lastEntryDate = entries.maxOfOrNull { it.timestamp } ?: return Pair(true, 0f)

        val daysLogged = (lastEntryDate - firstEntryDate) / (1000 * 60 * 60 * 24) + 1
        val averagePerDay = entries.size.toFloat() / daysLogged.toFloat()

        return if (daysLogged >= 30) {
            Pair(true, averagePerDay * 30)
        } else {
            Pair(false, averagePerDay * 30)
        }
    }

    //get yearly snus entries
    fun getYearlyAverage(entries: List<SnusEntry>): Pair<Boolean, Float> {
        if (entries.isEmpty()) return Pair(true, 0f)

        val firstEntryDate = entries.minOfOrNull { it.timestamp } ?: return Pair(true, 0f)
        val lastEntryDate = entries.maxOfOrNull { it.timestamp } ?: return Pair(true, 0f)

        val daysLogged = (lastEntryDate - firstEntryDate) / (1000 * 60 * 60 * 24) + 1
        val averagePerDay = entries.size.toFloat() / daysLogged.toFloat()

        return if (daysLogged >= 365) {
            Pair(true, averagePerDay * 365)
        } else {
            Pair(false, averagePerDay * 365)
        }
    }
    fun getWeeklyAverage(entries: List<SnusEntry>): Pair<Boolean, Float> {
        if (entries.isEmpty()) return Pair(true, 0f)

        val firstEntryDate = entries.minOfOrNull { it.timestamp } ?: return Pair(true, 0f)
        val lastEntryDate = entries.maxOfOrNull { it.timestamp } ?: return Pair(true, 0f)

        val daysLogged = (lastEntryDate - firstEntryDate) / (1000 * 60 * 60 * 24) + 1
        val averagePerDay = entries.size.toFloat() / daysLogged.toFloat()

        return if (daysLogged >= 7) {
            Pair(true, averagePerDay * 7)
        } else {
            Pair(false, averagePerDay * 7)
        }
    }

    fun calculateCost(averageConsumption: Float): Float {
        val packagesUsed = averageConsumption / portionsPerPackage.value!!
        return packagesUsed * costPerPackage.value!!
    }

    // Method to update cost and portions
    fun updateCostPerPackage(newCost: Int) {
        settingsManager.costPerPackage = newCost.toFloat()
        (costPerPackage as MutableLiveData).value = newCost.toFloat()
    }

    fun updatePortionsPerPackage(newPortions: Int) {
        settingsManager.portionsPerPackage = newPortions.toFloat()
        (portionsPerPackage as MutableLiveData).value = newPortions.toFloat()
    }


    fun updateDarkModeEnabled(isEnabled: Boolean) {
        settingsManager.darkModeOn = isEnabled
        darkModeEnabled.value = isEnabled
    }

}
