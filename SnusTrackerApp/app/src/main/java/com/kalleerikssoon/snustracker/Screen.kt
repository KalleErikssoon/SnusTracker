package com.kalleerikssoon.snustracker

sealed class Screen(val route: String) {
        data object Home : Screen("home")
        data object MapView : Screen("map_view")
        data object Statistics : Screen("statistics")
        data object Settings : Screen("settings")
}