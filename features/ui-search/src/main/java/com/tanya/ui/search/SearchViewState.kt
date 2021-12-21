package com.tanya.ui.search

import com.tanya.data.results.ShowDetailed

internal data class SearchViewState(
    val query: String = "",
    val searchResults: List<ShowDetailed> = emptyList(),
    val refreshing: Boolean = false
) {
    companion object {
        val Empty = SearchViewState()
    }
}