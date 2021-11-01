package com.tanya.showhub.ui.components

import com.tanya.common.ui.resources.R
import com.tanya.showhub.Screen
import com.tanya.showhub.ui.NavigationItem

val navigationItems = listOf(
    NavigationItem(
        screen = Screen.Home,
        labelResId = R.string.home_title,
        iconResId = R.drawable.ic_home,
        selectedIconResId = R.drawable.ic_home_fill
    ),
    NavigationItem(
        screen = Screen.Search,
        labelResId = R.string.search_title,
        iconResId = R.drawable.ic_search,
        selectedIconResId = R.drawable.ic_search_fill
    ),
    NavigationItem(
        screen = Screen.Library,
        labelResId = R.string.library_title,
        iconResId = R.drawable.ic_library,
        selectedIconResId = R.drawable.ic_library_fill
    ),
    NavigationItem(
        screen = Screen.About,
        labelResId = R.string.about_title,
        iconResId = R.drawable.ic_showhub,
    )
)