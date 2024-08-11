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
