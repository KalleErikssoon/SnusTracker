package com.kalleerikssoon.snustracker

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
/**
 * Data access object (DAO) interface for accessing the Snus database.
 * Contains methods for performing different database operations.
 */
@Dao
interface SnusEntryDao {

    // get all entries in the database, sorted in descending order based on timestamp
    @Query("SELECT * FROM snus_table ORDER BY timestamp DESC")
    fun getAllEntries(): LiveData<List<SnusEntry>>

    // insert multiple snus entries into the database
    @Insert
    fun insertAll(vararg entries: SnusEntry)

    // delete a snus entry from the database
    @Delete
    fun delete(entry: SnusEntry)

    // Retrieves snus entries for the current day from the database.
    // Uses Unix epoch time and date functions to filter entries for today.
    @Query("SELECT * FROM snus_table WHERE date(timestamp / 1000, 'unixepoch') = date('now')")
    fun getEntriesForToday(): LiveData<List<SnusEntry>>

    // Retrieves snus entries for the current week
    @Query("""
        SELECT * FROM snus_table 
        WHERE timestamp >= date('now', 'weekday 0', '-7 days')
    """)
    fun getEntriesForWeek(): LiveData<List<SnusEntry>>

    // retrieves snus entries for the current month
    // Uses strftime function to filter entries for the current year and month.
    @Query("SELECT * FROM snus_table WHERE strftime('%Y-%m', timestamp) = strftime('%Y-%m', 'now')")
    fun getEntriesForMonth(): LiveData<List<SnusEntry>>

    // Retrieves snus entries for the current year
    //Uses strftime function to filter entries for the current year.
    @Query("SELECT * FROM snus_table WHERE strftime('%Y', timestamp) = strftime('%Y', 'now')")
    fun getEntriesForYear(): LiveData<List<SnusEntry>>
}
