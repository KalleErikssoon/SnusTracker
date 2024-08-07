package com.kalleerikssoon.snustracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room

/**
 * Factory class for creating an instance of SnusViewModel.
 * Initializes the Room database, DAO, and repository required for the ViewModel.
 */
class SnusViewModelFactory(
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    /**
     * Creates an instance of the specified ViewModel class.
     * If the requested ViewModel is SnusViewModel, it initializes the database, DAO, and repository.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return An instance of the specified ViewModel class.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SnusViewModel::class.java)) {
            val database = Room.databaseBuilder(
                application,
                SnusDatabase::class.java, "snusDB"
            ).build()
            val dao = database.snusEntryDao()
            val repository = SnusRepository(dao)
            @Suppress("UNCHECKED_CAST")
            return SnusViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
