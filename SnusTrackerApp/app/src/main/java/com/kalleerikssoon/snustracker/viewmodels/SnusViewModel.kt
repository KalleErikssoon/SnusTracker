package com.kalleerikssoon.snustracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kalleerikssoon.snustracker.utils.LocationHandler
import com.kalleerikssoon.snustracker.utils.UserSettings
import com.kalleerikssoon.snustracker.database.SnusEntry
import com.kalleerikssoon.snustracker.database.SnusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Class responsible for managing the data and business logic of the application,
 * It handles data retrieval, insertion, and deletion for the Room database
 * of snus entries, as well as managing location services and user preferences.
 * Interacts with the SnusRepository for database operations and the
 * LocationHandler for handling location-related tasks.
 */
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
            insert(entry)
        }
    }

    // get users current location
    fun fetchCurrentLocation() {
        locationHandler.getCurrentLocation { latitude, longitude ->
            _currentLocation.postValue(LatLng(latitude, longitude))
        }
    }

    // Internal function to insert the entry into the database
    fun insert(entry: SnusEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entry)
    }

    // Function to delete a snus entry from the database
    fun delete(entry: SnusEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entry)

    }

    //get monthly average of snus consumed
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

    //get yearly average snus entries
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
    // get weekly average snus entries
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

    // calculate the estimated cost for the amount of snus consumed
    fun calculateCost(averageConsumption: Float): Float {
        val packagesUsed = averageConsumption / portionsPerPackage.value!!
        return packagesUsed * costPerPackage.value!!
    }

    // function to update cost per package
    fun updateCostPerPackage(newCost: Int) {
        settingsManager.costPerPackage = newCost.toFloat()
        (costPerPackage as MutableLiveData).value = newCost.toFloat()
    }

    // function to update portions per package
    fun updatePortionsPerPackage(newPortions: Int) {
        settingsManager.portionsPerPackage = newPortions.toFloat()
        (portionsPerPackage as MutableLiveData).value = newPortions.toFloat()
    }

    // function to update if darkmode is enabled or not
    fun updateDarkModeEnabled(isEnabled: Boolean) {
        settingsManager.darkModeOn = isEnabled
        darkModeEnabled.value = isEnabled
    }

}
