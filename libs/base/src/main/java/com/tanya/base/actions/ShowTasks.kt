package com.tanya.base.actions

interface ShowTasks {
    fun syncShowWatchedEpisodes(showId: Long)
    fun syncFollowedShows()
    fun syncFollowedShowsWhenIdle()
    fun setupNightSyncs()
}