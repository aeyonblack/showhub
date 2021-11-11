package com.tanya.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import app.showhub.common.compose.components.AppBar
import app.showhub.common.compose.utils.rememberFlowWithLifeCycle

@Composable
fun Home() {
    Home(
        viewModel = hiltViewModel()
    )
}

@Composable
internal fun Home(
    viewModel: HomeViewModel
) {
    val viewState by rememberFlowWithLifeCycle(flow = viewModel.state)
        .collectAsState(initial = HomeViewState.EMPTY)
    Home(
        state = viewState
    )
}

@Composable
internal fun Home(
    state: HomeViewState
) {
    Scaffold(
        topBar = { AppBar("Hello!")},
        modifier = Modifier.fillMaxSize()
    ) {
    }
}