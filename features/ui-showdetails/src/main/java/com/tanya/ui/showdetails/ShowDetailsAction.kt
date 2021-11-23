package com.tanya.ui.showdetails

internal sealed class ShowDetailsAction {
    object NavigateUp : ShowDetailsAction()
    data class OpenShowDetails(val showId: Long): ShowDetailsAction()
}