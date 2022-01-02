package com.tanya.showhub.ui.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.showhub.common.compose.extensions.actionButtonBackground
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
    /*Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Premium") },
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyBottom = false
                ),
                elevation = 0.dp,
                backgroundColor = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {

    }*/
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            item {
                Text(
                    text = "Premium features coming soon",
                    style = MaterialTheme.typography.h4,
                    color = Color.White.copy(1f),
                    modifier = Modifier.padding(top = 64.dp)
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "PREMIUM COMING SOON",
                        style = MaterialTheme.typography.h5,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .actionButtonBackground(
                                enabled = true,
                                alpha = 1f,
                                shape = RoundedCornerShape(32.dp)
                            )
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                    )
                }
            }
        }
    }
}
