package com.tanya.showhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import app.showhub.common.compose.theme.ShowhubTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.tanya.showhub.ui.components.BottomNavBar
import com.tanya.showhub.ui.components.currentScreenAsState
import dagger.hilt.android.AndroidEntryPoint
import com.google.accompanist.insets.ui.Scaffold

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            val selectedItem by navHostController.currentScreenAsState()

            WindowCompat.setDecorFitsSystemWindows(window, false)

            ProvideWindowInsets(consumeWindowInsets = false) {
                ShowhubTheme {
                    /*TODO(remember to move this somewhere else)*/
                    Scaffold(
                        bottomBar = {
                            BottomNavBar(
                                selectedNavigation = selectedItem,
                                onNavigationSelected = {
                                    navHostController.navigate(it.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(navHostController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                    }
                                }
                            )
                        },
                        backgroundColor = MaterialTheme.colors.background
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            AppNavigation(
                                navHostController = navHostController,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }
        }
    }
}