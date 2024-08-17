package com.kalleerikssoon.snustracker.utils

/**
 * Sealed class used to define the different screens in the apps navigation graph.
 * Each screen is represented as a data object with a string, which is used for navigation.
 *
 * @property route string representing the route to the screen, used for navigation within the app.
 */
sealed class Screen(val route: String) {
        data object Home : Screen("home")
        data object MapView : Screen("map_view")
        data object Statistics : Screen("statistics")
        data object Settings : Screen("settings")
}