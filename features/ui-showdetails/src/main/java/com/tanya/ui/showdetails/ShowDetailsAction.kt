package com.tanya.ui.showdetails

internal sealed class ShowDetailsAction {
    data class OpenShowDetails(val showId: Long): ShowDetailsAction()
}