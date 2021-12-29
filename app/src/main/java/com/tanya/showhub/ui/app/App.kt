package com.tanya.showhub.ui.app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import app.showhub.common.compose.components.AppBar
import com.google.accompanist.insets.ui.Scaffold
import com.tanya.showhub.AppNavigation
import com.tanya.showhub.Screen
import com.tanya.showhub.ui.components.BottomNavBar

@Composable
internal fun App(
    navController: NavHostController,
    selectedNavigation: Screen,
    onNavigationSelected: (Screen) -> Unit
) {
    var hideBottomBar by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedNavigation = selectedNavigation,
                navController = navController,
                hideBottomBar = hideBottomBar,
                onNavigationSelected = onNavigationSelected
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            AppNavigation(
                navHostController = navController,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                hideBottomBar = it
            }
        }
    }
}

@Composable
internal fun About() {
    Scaffold(
        topBar = { AppBar("About")},
        modifier = Modifier.fillMaxSize()
    ) {
    }
}