package com.tanya.showhub.extensions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.tanya.showhub.LeafScreen
import com.tanya.showhub.Screen

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

}

fun NavGraphBuilder.addLibraryTopLevel(
    navController: NavController
) {

}

fun NavGraphBuilder.addAboutTopLevel(
    navController: NavController
) {

}

fun NavGraphBuilder.addHome(
    navController: NavController,
    root: Screen
) {

}