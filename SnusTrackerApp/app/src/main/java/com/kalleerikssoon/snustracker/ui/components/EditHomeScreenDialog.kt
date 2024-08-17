package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kalleerikssoon.snustracker.utils.TimePeriod

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

/**
 * Composable that displays a dialog allowing the user to select a time period
 * to be dislpayed. Contains a list of radio buttons representing different time periods,
 * and buttons to cancel/confirm the user choices
 * @param currentPeriod The currently selected time period
 * @param onPeriodSelected Callback to handle the selected time period when the user confirms their choice.
 * @param onDismiss Callback to handle the dismissal of the dialog
 */
@Composable
fun EditHomeScreenDialog(
    currentPeriod: String,
    onPeriodSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf(currentPeriod) }

    val timePeriods = listOf("Daily") + TimePeriod.entries.map { it.name }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Time Period To Display") },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                timePeriods.forEach { period ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        RadioButton(
                            selected = period == selectedPeriod,
                            onClick = { selectedPeriod = period }
                        )
                        Text(text = period)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onPeriodSelected(selectedPeriod)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

