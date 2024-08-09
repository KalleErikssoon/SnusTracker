package com.kalleerikssoon.snustracker.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  Class representing a Snus entity with timestamp, location and auto generated ID
 */

@Entity(tableName = "snus_table")
data class SnusEntry(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)