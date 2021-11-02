package com.tanya.showhub.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import app.showhub.common.compose.theme.compositedOnBackground
import app.showhub.common.compose.theme.pink500
import com.google.accompanist.insets.navigationBarsPadding
import com.tanya.showhub.Screen
import com.tanya.showhub.ui.NavigationItem
import java.util.*


@Composable
internal fun BottomNavBar(
    selectedNavigation: Screen
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        modifier = Modifier.fillMaxWidth(),
    ) {
        navigationItems.forEach {
            val selected = selectedNavigation == it.screen
            BottomNavigationItem(
                icon = {
                       NavigationItemIcon(item = it, selected = selected)
                },
                label = {
                        Text(
                            text = stringResource(id = it.labelResId),
                            fontSize = 10.sp
                        )
                },
                selected = selected,
                //unselectedContentColor = MaterialTheme.colors.onSurface,
                onClick = {/*TODO*/},
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun NavigationItemIcon(
    item: NavigationItem,
    selected: Boolean
) {
    val painter = painterResource(id = item.iconResId)
    val selectedPainter = item.selectedIconResId?.let { painterResource(id = it) }
    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = null,
                modifier = if (it) Modifier.alpha(1f) else Modifier.alpha(0.8f)
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = if (selected) Modifier.alpha(1f) else Modifier.alpha(0.8f)
        )
    }
}