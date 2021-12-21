package com.tanya.ui.search

internal sealed class SearchAction {
    data class OpenShowDetails(val showId: Long): SearchAction()
    data class Search(val searchTerm: String = ""): SearchAction()
}