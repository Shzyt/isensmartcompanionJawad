package fr.isen.ghitri.isensmartcompanion.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import fr.isen.ghitri.isensmartcompanion.ui.EventsScreen
import fr.isen.ghitri.isensmartcompanion.ui.HistoryScreen
import fr.isen.ghitri.isensmartcompanion.ui.HomeScreen


@Composable
fun MaNavBar() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = nav_bar_item.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(nav_bar_item.Home.route) {
                HomeScreen()
            }
            composable(nav_bar_item.Events.route) {
                EventsScreen()
            }
            composable(nav_bar_item.History.route) {
                HistoryScreen()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        nav_bar_item.Home,
        nav_bar_item.Events,
        nav_bar_item.History
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            val icon = when (item) {
                nav_bar_item.Home -> Icons.Default.Home
                nav_bar_item.Events -> Icons.Default.Notifications
                nav_bar_item.History -> Icons.Default.Refresh
            }


            NavigationBarItem(
                icon = { Icon(
                    imageVector = icon,
                    contentDescription = item.labelResId
                ) },
                label = { Text(item.labelResId) },
                selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true

                        restoreState = true
                    }
                }
            )
        }
    }
}