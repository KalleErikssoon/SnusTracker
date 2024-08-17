package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kalleerikssoon.snustracker.R
import com.kalleerikssoon.snustracker.utils.TimePeriod

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

/**
 * A composable that displays statistics for snus consumption,
 * includes total snus usage, average consumption, and estimated cost over different time period.
 * The content is organized within scrollable cards, with edit options for portions per package
 * and cost per package.
 *
 * @param totalSnus The total number of snus portions consumed.
 * @param averageSnus A pair representing whether the average is actual or estimated, and the average consumption.
 * @param estimatedCost The estimated cost based on consumption and cost per package.
 * @param timePeriod The time period for the statistics displayed.
 * @param portionsPerPackage The number of portions per snus package.
 * @param onEditCostClick Callback function to handle editing the cost per package.
 * @param onEditPortionClick Callback function to handle editing the portions per package.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun StatisticsContent(
    totalSnus: Int,
    averageSnus: Pair<Boolean, Float>,
    estimatedCost: Float,
    timePeriod: TimePeriod,
    portionsPerPackage: Float,
    onEditCostClick: () -> Unit,
    onEditPortionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val estimatedPackages = averageSnus.second / portionsPerPackage
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        if (timePeriod != TimePeriod.Total) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = "Your Icon",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Total Snus $timePeriod",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$totalSnus portions",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chart),
                    contentDescription = "Your Icon",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (averageSnus.first) "Actual Consumption $timePeriod" else "Estimated Consumption $timePeriod",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "${"%.0f".format(averageSnus.second)} portions",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${"%.2f".format(estimatedPackages)} packages",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        IconButton(onClick = onEditPortionClick) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Portions")
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.money),
                    contentDescription = "Your Icon",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Estimated Cost",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${"%.2f".format(estimatedCost)} kr",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onEditCostClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Cost")
                }
            }
        }
    }
}



