package com.tanya.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.showhub.common.compose.components.AppBar

@Composable
fun Home() {
    Scaffold(
        topBar = { AppBar("Hello!")},
        modifier = Modifier.fillMaxSize()
    ) {
    }
}