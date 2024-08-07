package com.kalleerikssoon.snustracker

import android.util.Log
import androidx.lifecycle.LiveData

/**
 * Repository class for managing SnusEntry data.
 * Provides functions to perform database operations and access data using the SnusEntryDao interface.
 */
class SnusRepository(private val snusEntryDao: SnusEntryDao) {
    // LiveData for observing all snus entries
    val allEntries: LiveData<List<SnusEntry>> = snusEntryDao.getAllEntries()

    // Function to get snus entries for today
    fun getEntriesForToday(): LiveData<List<SnusEntry>> {
        return snusEntryDao.getEntriesForToday()
    }
    // Function to get snus entries for the week
    fun getEntriesForWeek(): LiveData<List<SnusEntry>> {
        return snusEntryDao.getEntriesForWeek()
    }
    // Function to get snus entries for the month
    fun getEntriesForMonth(): LiveData<List<SnusEntry>> {
        return snusEntryDao.getEntriesForMonth()
    }
    // Function to get snus entries for the year
    fun getEntriesForYear(): LiveData<List<SnusEntry>> {
        return snusEntryDao.getEntriesForYear()
    }
    // Function to add a snus entry
    fun insert(entry: SnusEntry) {
        snusEntryDao.insertAll(entry)
        Log.d("SnusRepository", "Inserted entry: $entry")
    }
    // Function to delete a snus entry
    fun delete(entry: SnusEntry) {
        snusEntryDao.delete(entry)
        Log.d("SnusRepository", "Deleted entry: $entry")
    }
}
