package com.kalleerikssoon.snustracker.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * class that provides an interface for accessing and modifying user settings
 * stored in SharedPreferences. Handles settings like cost per package, portions per package,
 * home screen period, and dark mode preference.
 *
 * @property costPerPackage cost per package of snus, stored as a Float.
 * @property portionsPerPackage number of portions per package, stored as a Float.
 * @property homeScreenPeriod selected time period for the home screen, stored as a String.
 * @property darkModeOn Boolean indicating whether dark mode is enabled.
 */
class UserSettingsHelper(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)

    var costPerPackage: Float
        get() = preferences.getFloat("cost_per_package", 30f)
        set(value) = preferences.edit().putFloat("cost_per_package", value).apply()

    var portionsPerPackage: Float
        get() = preferences.getFloat("portions_per_package", 20f)
        set(value) = preferences.edit().putFloat("portions_per_package", value).apply()
    var homeScreenPeriod: String
        get() = preferences.getString("home_screen_period", "Daily") ?: "Daily"
        set(value) = preferences.edit().putString("home_screen_period", value).apply()
    var darkModeOn: Boolean
        get() = preferences.getBoolean("dark_mode", false)
        set(value) = preferences.edit().putBoolean("dark_mode", value).apply()
}

/**
 * Singleton object that provides access to the UserSettingsHelper.
 * Ensures that UserSettingsHelper is only initialized once and provides a way to access
 * the instance from anywhere in the app.
 */
object UserSettings {
    private var instance: UserSettingsHelper? = null

    fun initialize(context: Context) {
        if (instance == null) {
            instance = UserSettingsHelper(context)
        }
    }

    fun getInstance(): UserSettingsHelper {
        return instance ?: throw IllegalStateException("UserSettings not initialized")
    }
}