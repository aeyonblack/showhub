package com.tanya.showhub

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.tanya.showhub.extensions.addAboutTopLevel
import com.tanya.showhub.extensions.addHomeTopLevel
import com.tanya.showhub.extensions.addLibraryTopLevel
import com.tanya.showhub.extensions.addSearchTopLevel

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
sealed class LeafScreen(private val route: String) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object Home: LeafScreen("home")
    object Search: LeafScreen("search")
    object Library: LeafScreen("library")
    object About: LeafScreen("about")
    object Trending: LeafScreen("trending")
    object Popular: LeafScreen("popular")

    object ShowDetails: LeafScreen("show/{showId}") {
        fun createRoute(root: Screen, showId: Long) = "${root.route}/show/$showId"
    }
}

/**
 * Orchestrates navigation and defines
 * the relationships between screens
 */
@Composable
internal fun AppNavigation(
    navHostController: NavHostController,
    modifier:Modifier = Modifier,
    onHideBottomBar: ((Boolean) -> Unit)?
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        addHomeTopLevel(navHostController)
        addSearchTopLevel(navHostController)
        addLibraryTopLevel(navHostController, onHideBottomBar)
        addAboutTopLevel(navHostController)
    }
}


