package com.tanya.ui.showdetails

import com.tanya.data.entities.ActionDate

internal sealed class ShowDetailsAction {
    object NavigateUp : ShowDetailsAction()
    object FollowShowToggleAction: ShowDetailsAction()
    object RefreshAction: ShowDetailsAction()

    data class MarkSeasonWatchedAction(
        val seasonId: Long,
        val onlyAired: Boolean = false,
        val date: ActionDate = ActionDate.NOW
    ): ShowDetailsAction()

    data class UpdateSeasonEpisodes(val episodeIds: List<Long>): ShowDetailsAction()

    data class MarkSeasonUnwatchedAction(val seasonId: Long): ShowDetailsAction()

    data class UnfollowPreviousSeasonsFollowed(val seasonId: Long): ShowDetailsAction()

    data class ChangeSeasonFollowedAction(
        val seasonId: Long,
        val followed: Boolean
    ): ShowDetailsAction()

    data class OpenShowDetails(val showId: Long): ShowDetailsAction()
}