package com.tanya.ui.home

import androidx.compose.runtime.Immutable
import com.tanya.data.results.PopularEntryWithShow
import com.tanya.data.results.TrendingEntryWithShow

@Immutable
internal data class HomeViewState(
    val trendingItems: List<TrendingEntryWithShow> = emptyList(),
    val trendingRefreshing: Boolean = false,
    val popularItems: List<PopularEntryWithShow> = emptyList(),
    val popularRefreshing: Boolean = false
) {
    val refreshing get() = trendingRefreshing || popularRefreshing

    companion object {
        val EMPTY = HomeViewState()
    }
}