package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun EditCostDialog(
    currentCost: Int,
    onSaveClick: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    var cost by remember { mutableIntStateOf(currentCost) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = { onSaveClick(cost) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Cost Per Package") },
        text = {
            Column {
                Text(text = "Cost per Package: $cost kr")
                Slider(
                    value = cost.toFloat(),
                    onValueChange = { cost = it.toInt() },
                    valueRange = 15f..90f,
                    steps = 90
                )
            }
        }
    )
}
