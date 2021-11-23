package com.tanya.ui.home

internal sealed class HomeAction {
    object RefreshAction: HomeAction()
    data class OpenShowDetails(val showId: Long): HomeAction()
}