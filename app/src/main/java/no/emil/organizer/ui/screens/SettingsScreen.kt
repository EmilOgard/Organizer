package no.emil.organizer.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item { SettingsSection("General") }

            item {
                SettingsItem(
                    title = "Example 1",
                    description = "Example text 1",
                    icon = Icons.Default.Notifications
                )
            }

            item {
                SettingsItem(
                    title = "Example 2",
                    description = "Example text 2",
                    icon = Icons.Default.Settings
                )
            }

            item {
                SettingsItem(
                    title = "Example 3",
                    description = "Example text 3",
                    icon = Icons.Default.DateRange
                )
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }


            item { SettingsSection("Visual") }

            item {
                SettingsItem(
                    title = "Example 4",
                    description = "Example text 4",
                    icon = Icons.Default.AccountCircle
                )
            }

            item {
                SettingsItem(
                    title = "Example 5",
                    description = "Example text 5",
                    icon = Icons.Default.Info
                )
            }
        }

    }
}

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    icon: ImageVector
) {
    var checked by remember { mutableStateOf(false) }

    ListItem(
        leadingContent = {
            Icon(icon, contentDescription = null)
        },
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
    )
}
