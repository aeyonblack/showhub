package com.tanya.ui.showdetails

import com.tanya.data.entities.ActionDate

internal sealed class ShowDetailsAction {
    object NavigateUp : ShowDetailsAction()
    object FollowShowToggleAction: ShowDetailsAction()

    data class MarkSeasonWatchedAction(
        val seasonId: Long,
        val onlyAired: Boolean = false,
        val date: ActionDate = ActionDate.NOW
    ): ShowDetailsAction()

    data class MarkSeasonUnWatchedAction(val seasonId: Long): ShowDetailsAction()

    data class ChangeSeasonFollowedAction(
        val seasonId: Long,
        val followed: Boolean
    ): ShowDetailsAction()

    data class OpenShowDetails(val showId: Long): ShowDetailsAction()
}