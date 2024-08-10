package com.kalleerikssoon.snustracker.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kalleerikssoon.snustracker.Screen
import com.kalleerikssoon.snustracker.SnusViewModel
import com.kalleerikssoon.snustracker.TimePeriod
import com.kalleerikssoon.snustracker.ui.components.BottomNavigationBar
import com.kalleerikssoon.snustracker.ui.components.StatisticsContent
import com.kalleerikssoon.snustracker.ui.components.TimePeriodTabs

@Composable
fun StatsScreen(viewModel: SnusViewModel, navController: NavHostController) {
    val currentPeriod = remember { mutableStateOf(TimePeriod.Weekly) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, currentScreen = Screen.Statistics) },
        topBar = {
            TimePeriodTabs(
                currentPeriod = currentPeriod.value,
                onPeriodSelected = { period ->
                    currentPeriod.value = period
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    ) { paddingValues ->
        val entries = when (currentPeriod.value) {
            TimePeriod.Weekly -> viewModel.getEntriesForWeek().observeAsState(emptyList()).value
            TimePeriod.Monthly -> viewModel.getEntriesForMonth().observeAsState(emptyList()).value
            TimePeriod.Yearly -> viewModel.getEntriesForYear().observeAsState(emptyList()).value
            TimePeriod.Total -> viewModel.getAllEntries().observeAsState(emptyList()).value
        }

        // Calculate the average based on the selected time period
        val average = when (currentPeriod.value) {
            TimePeriod.Weekly -> viewModel.getWeeklyAverage(entries)
            TimePeriod.Monthly -> viewModel.getMonthlyAverage(entries)
            TimePeriod.Yearly -> viewModel.getYearlyAverage(entries)
            TimePeriod.Total -> Pair(true, entries.size.toFloat())
        }

        // Calculate the estimated cost
        val estimatedCost = viewModel.calculateCost(average.second)

        StatisticsContent(
            totalSnus = entries.size,
            averageSnus = average,
            timePeriod = currentPeriod.value,
            estimatedCost = estimatedCost,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

