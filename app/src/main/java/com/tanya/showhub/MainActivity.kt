package com.tanya.showhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import app.showhub.common.compose.theme.ShowhubTheme
import app.showhub.common.compose.utils.LocalShowhubDateTimeFormatter
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.ui.Scaffold
import com.tanya.base.android.appinitializer.util.ShowhubDateFormatter
import com.tanya.showhub.ui.components.BottomNavBar
import com.tanya.showhub.ui.components.currentScreenAsState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject internal lateinit var showhubDateFormatter: ShowhubDateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val selectedItem by navController.currentScreenAsState()

            WindowCompat.setDecorFitsSystemWindows(window, false)

            CompositionLocalProvider(
                LocalShowhubDateTimeFormatter provides showhubDateFormatter
            ) {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    ShowhubTheme {
                        /*TODO(remember to move this somewhere else)*/
                        Scaffold(
                            bottomBar = {
                                BottomNavBar(
                                    selectedNavigation = selectedItem,
                                    navController = navController,
                                    onNavigationSelected = {
                                        navController.navigate(it.route) {
                                            launchSingleTop = true
                                            restoreState = true
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                        }
                                    },
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
                }
            }
        }
    }
}