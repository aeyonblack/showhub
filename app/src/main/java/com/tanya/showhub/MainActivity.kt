package com.tanya.showhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import app.showhub.common.compose.components.AppBar
import app.showhub.common.compose.components.BottomAppBar
import app.showhub.common.compose.theme.ShowhubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowhubTheme {
                Scaffold(
                    topBar = { AppBar() },
                    bottomBar = { BottomAppBar() },
                    backgroundColor = MaterialTheme.colors.background
                ) {

                }
            }
        }
    }
}