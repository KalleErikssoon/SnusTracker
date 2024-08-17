package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable that displays a dialog for editing the amount of snus portions per package.
 * It includes a slider for adjusting the amount within a specified range and buttons to
 * save or cancel the changes.
 *
 * @param currentCost The current amount of portions per package to be displayed initially.
 * @param onSaveClick Callback to handle saving the new amount of portions when the user confirms their choice.
 * @param onDismissRequest Callback to handle the dismissal of the dialog without saving changes.
 */
@Composable
fun EditPortionDialog(
    currentPortions: Int,
    onSaveClick: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    var portions by remember { mutableIntStateOf(currentPortions) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = { onSaveClick(portions) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = { Text("Edit Portions Per Package") },
        text = {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Portions per Package: $portions")
                Slider(
                    value = portions.toFloat(),
                    onValueChange = { portions = it.toInt() },
                    valueRange = 20f..30f,
                    steps = 10
                )
            }
        }
    )
}
