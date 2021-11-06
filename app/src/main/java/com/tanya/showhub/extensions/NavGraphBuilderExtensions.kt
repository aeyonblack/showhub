package com.tanya.showhub.extensions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.tanya.showhub.LeafScreen
import com.tanya.showhub.Screen
import com.tanya.showhub.ui.home.About
import com.tanya.showhub.ui.home.Library
import com.tanya.showhub.ui.home.Search
import com.tanya.ui.home.Home

/*These extension functions are for adding top level
* destinations and all destinations that can be reached from them
* ---------------------------------------------------------------*/

fun NavGraphBuilder.addHomeTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.createRoute(Screen.Home)
    ) {
        addHome(navController, Screen.Home)
    }
}

fun NavGraphBuilder.addSearchTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Search.route,
        startDestination = LeafScreen.Search.createRoute(Screen.Search)
    ) {
        addSearch(navController, Screen.Search)
    }
}

fun NavGraphBuilder.addLibraryTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Library.route,
        startDestination = LeafScreen.Library.createRoute(Screen.Library)
    ) {
        addLibrary(navController, Screen.Library)
    }
}

fun NavGraphBuilder.addAboutTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.About.route,
        startDestination = LeafScreen.About.createRoute(Screen.About)
    ) {
        addAbout(navController, Screen.About)
    }
}

fun NavGraphBuilder.addHome(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.Home.createRoute(Screen.Home)) {
        Home()
    }
}

fun NavGraphBuilder.addSearch(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.Search.createRoute(Screen.Search)) {
        Search()
    }
}

fun NavGraphBuilder.addLibrary(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.Library.createRoute(Screen.Library)) {
        Library()
    }
}

fun NavGraphBuilder.addAbout(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.About.createRoute(Screen.About)) {
        About()
    }
}
