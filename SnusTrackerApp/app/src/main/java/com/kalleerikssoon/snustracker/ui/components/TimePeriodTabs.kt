package com.kalleerikssoon.snustracker.ui.components


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kalleerikssoon.snustracker.TimePeriod

@Composable
fun TimePeriodTabs(
    currentPeriod: TimePeriod,
    onPeriodSelected: (TimePeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    val timePeriods = listOf(
        TimePeriod.Weekly to "Weekly",
        TimePeriod.Monthly to "Monthly",
        TimePeriod.Yearly to "Yearly",
        TimePeriod.Total to "Total"
    )

    val selectedTabIndex = timePeriods.indexOfFirst { it.first == currentPeriod }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        timePeriods.forEachIndexed { index, pair ->
            val (period, label) = pair
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onPeriodSelected(period) },
                text = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }
    }
}

