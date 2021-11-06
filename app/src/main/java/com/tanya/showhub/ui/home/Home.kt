package com.tanya.showhub.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.showhub.common.compose.components.AppBar


@Composable
internal fun Search() {
    Scaffold(
        topBar = { AppBar("Search")},
        modifier = Modifier.fillMaxSize()
    ) {
    }
}

@Composable
internal fun Library() {
    Scaffold(
        topBar = { AppBar("Your Library")},
        modifier = Modifier.fillMaxSize()
    ) {
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
