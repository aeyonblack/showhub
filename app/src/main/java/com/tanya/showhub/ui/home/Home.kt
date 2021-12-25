package com.tanya.showhub.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import app.showhub.common.compose.components.AppBar
import com.google.accompanist.insets.ui.Scaffold
import com.tanya.showhub.AppNavigation
import com.tanya.showhub.Screen
import com.tanya.showhub.ui.components.BottomNavBar

@Composable
internal fun Home(
    navController: NavHostController,
    selectedNavigation: Screen,
    onNavigationSelected: (Screen) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedNavigation = selectedNavigation,
                navController = navController,
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
            )
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
