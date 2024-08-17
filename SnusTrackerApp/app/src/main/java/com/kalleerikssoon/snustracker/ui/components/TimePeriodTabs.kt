package com.kalleerikssoon.snustracker.ui.components


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kalleerikssoon.snustracker.utils.TimePeriod

/**
 * A composable that displays tabs for switching between different time periods.
 * The selected period is highlighted, clicking on a tab triggers a callback to handle the selection.
 *
 * @param currentPeriod The currently selected time period.
 * @param onPeriodSelected Callback function to handle the selected time period when the user clicks a tab.
 * @param modifier Modifier to be applied to the TabRow.
 */
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

