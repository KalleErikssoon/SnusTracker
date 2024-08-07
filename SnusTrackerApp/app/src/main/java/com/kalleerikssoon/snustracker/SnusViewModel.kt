package com.kalleerikssoon.snustracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel class for managing SnusEntry data.
 * Interacts with the repository to perform database operations and provides data to the UI.
 */
class SnusViewModel(application: Application, private val repository: SnusRepository) : AndroidViewModel(application) {

    private val allEntries: LiveData<List<SnusEntry>> = repository.allEntries
    val todayEntries: LiveData<List<SnusEntry>> = repository.getEntriesForToday()

    // Function to get snus entries for the current week
    fun getEntriesForWeek(): LiveData<List<SnusEntry>> = repository.getEntriesForWeek()

    // Function to get snus entries for the current month
    fun getEntriesForMonth(): LiveData<List<SnusEntry>> = repository.getEntriesForMonth()

    // Function to get snus entries for the current year
    fun getEntriesForYear(): LiveData<List<SnusEntry>> = repository.getEntriesForYear()

    // Function to insert a snus entry into the database
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
        allEntries.observeForever { entries ->
            println("$operation - Current Entries: $entries")
        }
    }
}
