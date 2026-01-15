package no.emil.organizer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class NavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        NavItem("Notes", NavRoute.Notes.route, Icons.Filled.Edit),
        NavItem("Todo", NavRoute.Todo.route, Icons.Filled.Done),
        NavItem("Finance", NavRoute.Finance.route, Icons.Filled.ShoppingCart),
        NavItem("Calendar", NavRoute.Calendar.route, Icons.Filled.DateRange),
        NavItem("Settings", NavRoute.Settings.route, Icons.Filled.Settings),
    )

    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination == item.route,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}