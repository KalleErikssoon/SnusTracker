package com.kalleerikssoon.snustracker.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kalleerikssoon.snustracker.utils.Screen
import com.kalleerikssoon.snustracker.viewmodels.SnusViewModel
import com.kalleerikssoon.snustracker.utils.TimePeriod
import com.kalleerikssoon.snustracker.ui.components.BottomNavigationBar
import com.kalleerikssoon.snustracker.ui.components.EditCostDialog
import com.kalleerikssoon.snustracker.ui.components.EditPortionDialog
import com.kalleerikssoon.snustracker.ui.components.StatisticsContent
import com.kalleerikssoon.snustracker.ui.components.TimePeriodTabs

/**
 * A composable for the statistics screen of the app.
 * This screen displays the user's snus consumption statistics, including total consumption,
 * average consumption, and estimated cost for the selected time period. Users can switch
 * between different time periods using tabs at the top of the screen.
 * Includes functionality for editing the cost per package and portions per package via dialogs.
 *
 * @param viewModel SnusViewModel that provides data and functions for the Statistics screen.
 * @param navController NavHostController used for navigation between screens.
 */
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
        var showCostDialog by remember { mutableStateOf(false) }
        var showPortionDialog by remember { mutableStateOf(false) }

        if (showCostDialog) {
            EditCostDialog(
                currentCost = viewModel.costPerPackage.value!!.toInt(),
                onDismissRequest = { showCostDialog = false },
                onSaveClick = { newCost ->
                    viewModel.updateCostPerPackage(newCost)
                    showCostDialog = false
                }
            )
        }

        if (showPortionDialog) {
            EditPortionDialog(
                currentPortions = viewModel.portionsPerPackage.value!!.toInt(),
                onDismissRequest = { showPortionDialog = false },
                onSaveClick = { newPortions ->
                    viewModel.updatePortionsPerPackage(newPortions)
                    showPortionDialog = false
                }
            )
        }

        val portionsPerPackage = viewModel.portionsPerPackage.value!!

        StatisticsContent(
            totalSnus = entries.size,
            averageSnus = average,
            timePeriod = currentPeriod.value,
            estimatedCost = estimatedCost,
            portionsPerPackage = portionsPerPackage,
            onEditCostClick = { showCostDialog = true },
            onEditPortionClick = {showPortionDialog = true},
            modifier = Modifier.padding(paddingValues)
        )
    }
}


