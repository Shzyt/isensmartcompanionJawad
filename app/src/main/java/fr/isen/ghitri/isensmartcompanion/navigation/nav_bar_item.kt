package fr.isen.ghitri.isensmartcompanion.navigation

sealed class nav_bar_item(val route: String, val labelResId: String) {
    object Home : nav_bar_item("home", "Home")
    object Events : nav_bar_item("events", "Event")
    object History : nav_bar_item("history", "history")
}