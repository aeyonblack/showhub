package com.tanya.showhub.ui.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import app.showhub.common.compose.extensions.actionButtonBackground
import com.google.accompanist.insets.ui.Scaffold
import com.tanya.common.ui.resources.R.drawable.*
import com.tanya.common.ui.resources.R.string.about_description
import com.tanya.showhub.AppNavigation
import com.tanya.showhub.Screen
import com.tanya.showhub.ui.components.BottomNavBar

@Composable
internal fun App(
    navController: NavHostController,
    selectedNavigation: Screen,
    openGithubLink: () -> Unit,
    openLinkedInLink: () -> Unit,
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
                openGithubLink = openGithubLink,
                openLinkedInLink = openLinkedInLink,
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
internal fun About(
    openGithubLink: () -> Unit,
    openLinkedInLink: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Showhub",
                        style = MaterialTheme.typography.h4,
                        color = Color.White.copy(1f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 96.dp)
                            .align(CenterHorizontally)
                    )
                    Text(
                        text = stringResource(id = about_description),
                        style = MaterialTheme.typography.caption,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(CenterHorizontally)
                    )
                    PersonInfo(modifier = Modifier.align(CenterHorizontally))
                    ExternalLink(
                        icon = ic_github_complete,
                        text = "Github",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(CenterHorizontally),
                        onClick = openGithubLink
                    )
                    ExternalLink(
                        icon = ic_linkedin,
                        text = "LinkedIn",
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .align(CenterHorizontally),
                        onClick = openLinkedInLink
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

        }
    }
}

@Composable
private fun PersonInfo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.size(32.dp).align(CenterVertically)
        ) {
            Image(
                painter = painterResource(id = profile_2022),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = "Tanya Masvita, Developer",
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp).align(CenterVertically)
        )
    }
}

@Composable
private fun ExternalLink(
    icon: Int,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .actionButtonBackground(
                enabled = true,
                alpha = 1f,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .align(CenterVertically)
                .size(24.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
