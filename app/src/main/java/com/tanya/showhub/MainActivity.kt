package com.tanya.showhub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import app.showhub.common.compose.theme.ShowhubTheme
import app.showhub.common.compose.utils.LocalShowhubDateTimeFormatter
import com.google.accompanist.insets.ProvideWindowInsets
import com.tanya.base.android.appinitializer.util.ShowhubDateFormatter
import com.tanya.showhub.ui.app.App
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
                        App(
                            navController = navController,
                            selectedNavigation = selectedItem,
                            openGithubLink = { openLink("https://github.com/aeyonblack") },
                            openLinkedInLink = { openLink("https://www.linkedin.com/in/tanya-masvita") },
                            onNavigationSelected = {
                                navController.navigate(it.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(intent)
    }
}