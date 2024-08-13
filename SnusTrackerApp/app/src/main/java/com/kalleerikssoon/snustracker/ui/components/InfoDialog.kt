package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun InfoDialog(
    showDialog: MutableState<Boolean>,
    title: String = "About This App",
    infoText: String = "This app helps you track your snus consumption, view usage statistics, and locate where you've consumed snus on a map. You can also manage settings and estimate costs based on your usage."
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = infoText)
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}