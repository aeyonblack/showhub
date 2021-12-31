package com.tanya.showhub.extensions

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.tanya.showhub.LeafScreen
import com.tanya.showhub.Screen
import com.tanya.showhub.ui.app.About
import com.tanya.ui.home.Home
import com.tanya.ui.library.Library
import com.tanya.ui.popular.Popular
import com.tanya.ui.search.Search
import com.tanya.ui.showdetails.ShowDetails

/*These extension functions are for adding top level
* destinations and all destinations that can be reached from them
* ---------------------------------------------------------------*/

val topLevelDestinations = listOf(
    Screen.Home.route,
    Screen.Search.route,
    Screen.Library.route,
    Screen.About.route,
    LeafScreen.Home.createRoute(Screen.Home),
    LeafScreen.Search.createRoute(Screen.Search),
    LeafScreen.Library.createRoute(Screen.Library),
    LeafScreen.About.createRoute(Screen.About)
)

fun NavGraphBuilder.addHomeTopLevel(
    navController: NavController
) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.createRoute(Screen.Home)
    ) {
        addHome(navController, Screen.Home)
        addShowDetails(navController, Screen.Home)
        addPopularShows(navController, Screen.Home)
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
        addShowDetails(navController, Screen.Search)
    }
}

fun NavGraphBuilder.addLibraryTopLevel(
    navController: NavController,
    onHideBottomBar: ((Boolean) -> Unit)?
) {
    navigation(
        route = Screen.Library.route,
        startDestination = LeafScreen.Library.createRoute(Screen.Library)
    ) {
        addLibrary(navController, Screen.Library, onHideBottomBar)
        addShowDetails(navController, Screen.Library)
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
        Home(
            openShowDetails = { showId, _, _ ->
                navController.navigate(LeafScreen.ShowDetails.createRoute(root, showId))
            },
            openPopularShows = {
                navController.navigate(LeafScreen.Popular.createRoute(root))
            },
            openTrendingShows = {
                /*TODO*/
            }
        )
    }
}

fun NavGraphBuilder.addSearch(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.Search.createRoute(Screen.Search)) {
        Search(
            openShowDetails = {
                navController.navigate(LeafScreen.ShowDetails.createRoute(root, it))
            }
        )
    }
}

fun NavGraphBuilder.addLibrary(
    navController: NavController,
    root: Screen,
    onHideBottomBar: ((Boolean) -> Unit)?
) {
    composable(LeafScreen.Library.createRoute(Screen.Library)) {
        Library(
            openShowDetails = {
                navController.navigate(LeafScreen.ShowDetails.createRoute(root, it))
            },
            onHideBottomBar = onHideBottomBar
        )
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

fun NavGraphBuilder.addShowDetails(
    navController: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.ShowDetails.createRoute(root),
        arguments = listOf(
            navArgument("showId") {
                type = NavType.LongType
            }
        )
    ) {
        ShowDetails(
            navigateUp = navController::navigateUp,
            openShowDetails = {
                navController.navigate(LeafScreen.ShowDetails.createRoute(root, it))
            }
        )
    }
}

fun NavGraphBuilder.addPopularShows(
    navController: NavController,
    root: Screen
) {
    composable(LeafScreen.Popular.createRoute(root)) {
        Popular(
            openShowsDetails = {
                navController.navigate(LeafScreen.ShowDetails.createRoute(root, it))
            },
            navigateUp = navController::navigateUp
        )
    }
}

