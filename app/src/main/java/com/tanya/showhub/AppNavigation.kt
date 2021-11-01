package com.tanya.showhub

sealed class Screen(val route: String) {
    object Home: Screen("Home")
    object Search: Screen("Search")
    object Library: Screen("Library")
    object About: Screen("About")
}