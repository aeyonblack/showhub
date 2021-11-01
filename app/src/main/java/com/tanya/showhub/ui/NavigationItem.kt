package com.tanya.showhub.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tanya.showhub.Screen

/**
 * Represents a single item in the BottomNavBar
 */
data class NavigationItem(
    val screen: Screen,
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int,
    @DrawableRes val selectedIconResId: Int? = null,
)