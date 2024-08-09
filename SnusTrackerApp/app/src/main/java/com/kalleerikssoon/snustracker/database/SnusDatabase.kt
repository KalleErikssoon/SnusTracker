package com.kalleerikssoon.snustracker.database
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for snus entries
 * */
@Database(entities = [SnusEntry::class], version = 1)
abstract class SnusDatabase : RoomDatabase() {
    abstract fun snusEntryDao(): SnusEntryDao
}