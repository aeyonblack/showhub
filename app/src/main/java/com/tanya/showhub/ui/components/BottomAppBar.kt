package com.tanya.showhub.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.BottomNavigation
import com.tanya.showhub.Screen
import com.tanya.showhub.extensions.topLevelDestinations
import com.tanya.showhub.ui.NavigationItem


@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun BottomNavBar(
    selectedNavigation: Screen,
    navController: NavController,
    hideBottomBar: Boolean = false,
    onNavigationSelected: (Screen) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute in topLevelDestinations) {
        AnimatedVisibility(
            visible = !hideBottomBar,
            enter = slideInVertically() + fadeIn()
        ) {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.navigationBars
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                navigationItems.forEach {
                    val selected = selectedNavigation == it.screen
                    BottomNavigationItem(
                        icon = {
                            NavigationItemIcon(item = it, selected = selected)
                        },
                        label = {
                            Text(
                                text = stringResource(id = it.labelResId),
                                fontSize = 10.sp
                            )
                        },
                        selected = selected,
                        onClick = {
                            onNavigationSelected(it.screen)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationItemIcon(
    item: NavigationItem,
    selected: Boolean
) {
    val painter = painterResource(id = item.iconResId)
    val selectedPainter = item.selectedIconResId?.let { painterResource(id = it) }
    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = null,
                modifier = if (it) Modifier.alpha(1f) else Modifier.alpha(0.8f)
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = if (selected) Modifier.alpha(1f) else Modifier.alpha(0.8f)
        )
    }
}

@Stable
@Composable
fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Home) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Home.route } -> {
                    selectedItem.value = Screen.Home
                }
                destination.hierarchy.any { it.route == Screen.Search.route } -> {
                    selectedItem.value = Screen.Search
                }
                destination.hierarchy.any { it.route == Screen.Library.route } -> {
                    selectedItem.value = Screen.Library
                }
                destination.hierarchy.any { it.route == Screen.About.route } -> {
                    selectedItem.value = Screen.About
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}