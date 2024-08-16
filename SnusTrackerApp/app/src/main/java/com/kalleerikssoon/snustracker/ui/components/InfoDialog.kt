package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InfoDialog(
    showDialog: MutableState<Boolean>
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = "About This App",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    ScreenInfo(
                        screenName = "Home Screen",
                        description = "Allows you to add/delete snus entries with the buttons on the lower part of the screen, and displays the amount of snus used in different time periods (can be edited with the edit button in the top right corner)."
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ScreenInfo(
                        screenName = "Map View",
                        description = "Displays a map with the locations where you consumed snus."
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ScreenInfo(
                        screenName = "Statistics",
                        description = "Shows detailed statistics including usage, average/estimated consumption and cost across different time periods."
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ScreenInfo(
                        screenName = "Settings",
                        description = "Manage your preferences, including dark mode, cost per snus package/portions per package and more."
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text("OK")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp) // Adjust padding as needed
        )
    }
}

@Composable
fun ScreenInfo(screenName: String, description: String) {
    Text(
        text = buildAnnotatedString {
            // Apply bold to screen name
            append(
                AnnotatedString(
                    text = "$screenName\n",
                    spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                )
            )
            // Normal style for description
            append(description)
        },
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Start
    )
}