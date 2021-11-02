package com.tanya.showhub

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

/**
 * Top level destinations that can be accessed
 * by clicking a BottomNavBarItem
 */
sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Search: Screen("search")
    object Library: Screen("library")
    object About: Screen("about")
}

/**
 * Destinations that can only be reached from a top level
 * destination or other leaf destinations
 */
sealed class LeafScreen(val route: String) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object Home: LeafScreen("home")
    object Search: LeafScreen("search")
    object Library: LeafScreen("library")
    object About: LeafScreen("about")
}

/**
 * Orchestrates navigation and defines
 * the relationships between screens
 */
@Composable
internal fun AppNavigation(
    navHostController: NavHostController,
    modifier:Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

    }
}


