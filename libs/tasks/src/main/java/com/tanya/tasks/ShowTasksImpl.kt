package com.tanya.tasks

import androidx.work.WorkManager
import com.tanya.base.actions.ShowTasks
import javax.inject.Inject

class ShowTasksImpl @Inject constructor(
    private val workManager: WorkManager
) : ShowTasks {

    override fun syncShowWatchedEpisodes(showId: Long) {
    }

    override fun syncFollowedShows() {
        TODO("Not yet implemented")
    }

    override fun syncFollowedShowsWhenIdle() {
        TODO("Not yet implemented")
    }

    override fun setupNightSyncs() {
        TODO("Not yet implemented")
    }

}