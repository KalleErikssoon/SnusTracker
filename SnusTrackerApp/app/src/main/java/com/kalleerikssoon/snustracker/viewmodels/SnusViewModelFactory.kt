package com.kalleerikssoon.snustracker.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kalleerikssoon.snustracker.utils.LocationHandler
import com.kalleerikssoon.snustracker.database.SnusDatabase
import com.kalleerikssoon.snustracker.database.SnusRepository

/**
 * Factory class for creating an instance of SnusViewModel.
 * Initializes the Room database, DAO, repository, and LocationHandler required for the ViewModel.
 */
class SnusViewModelFactory(
    private val application: Application,
    private val locationHandler: LocationHandler // Declare locationHandler as a property
) : ViewModelProvider.AndroidViewModelFactory(application) {

    /**
     * Creates an instance of the specified ViewModel class.
     * If the requested ViewModel is SnusViewModel, it initializes the database, DAO, repository, and LocationHandler.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return An instance of the specified ViewModel class.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SnusViewModel::class.java)) {
            // Initialize the database, DAO, repository, and location handler here
            val database = Room.databaseBuilder(
                application,
                SnusDatabase::class.java, "snusDB"
            ).build()
            val dao = database.snusEntryDao()
            val repository = SnusRepository(dao)

            @Suppress("UNCHECKED_CAST")
            return SnusViewModel(application, repository, locationHandler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
