package com.kalleerikssoon.snustracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
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

    val todayEntries: LiveData<List<SnusEntry>> = repository.getEntriesForToday()

    private val averageCostPerPackage = 30.0 // Cost of one snus package in the local currency
    private val portionsPerPackage = 22 // Average portions per package

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

    fun calculateCost(averageConsumption: Float): Double {
        val packagesUsed = averageConsumption / portionsPerPackage
        return packagesUsed * averageCostPerPackage
    }

}
