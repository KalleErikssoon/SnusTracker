package com.kalleerikssoon.snustracker

import android.content.Context
import android.content.SharedPreferences

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
}
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