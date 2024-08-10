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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kalleerikssoon.snustracker.R
import com.kalleerikssoon.snustracker.TimePeriod

@Composable
fun StatisticsContent(
    totalSnus: Int,
    averageSnus: Pair<Boolean, Float>,
    estimatedCost: Double,
    timePeriod: TimePeriod,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Card for Total Snus
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

        // Card for Average Snus
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
                Column {
                    val averageText = if (averageSnus.first) {
                        "Actual Average"
                    } else {
                        "Estimated Average"
                    }
                    Text(
                        text = averageText,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${"%.0f".format(averageSnus.second)} portions",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Card for Estimated Cost
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
            }
        }
    }
}


