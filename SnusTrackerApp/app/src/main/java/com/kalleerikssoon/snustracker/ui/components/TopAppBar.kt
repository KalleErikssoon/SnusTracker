package com.kalleerikssoon.snustracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

/**
 * A composable for the top app bar for the home screen.
 * The app bar includes a title and two action buttons for displaying information about the app
 * and for editing the displayed time period.
 *
 * @param onInfoClick Callback function to handle the click the info icon.
 * @param onEditClick Callback function to handle clicking the edit icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAppBar(
    onInfoClick: () -> Unit,
    onEditClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "SnusTracker")
        },
        actions = {
            IconButton(onClick = onInfoClick) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = "Info about the app"
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit displayed time period"
                )
            }
        }
    )
}
