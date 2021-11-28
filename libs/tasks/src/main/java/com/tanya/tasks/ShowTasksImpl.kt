package com.tanya.tasks

import androidx.work.*
import com.tanya.base.actions.ShowTasks
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ShowTasksImpl @Inject constructor(
    private val workManager: WorkManager
) : ShowTasks {

    override fun syncShowWatchedEpisodes(showId: Long) {
        val request = OneTimeWorkRequest.Builder(SyncShowWatchedProgress::class.java)
            .addTag(SyncShowWatchedProgress.TAG)
            .setInputData(SyncShowWatchedProgress.buildData(showId))
            .build()
        workManager.enqueue(request)
    }

    override fun syncFollowedShows() {
        val request = OneTimeWorkRequest.Builder(SyncAllFollowedShows::class.java)
            .addTag(SyncAllFollowedShows.TAG)
            .build()
        workManager.enqueue(request)
    }

    override fun syncFollowedShowsWhenIdle() {
        val request = OneTimeWorkRequest.Builder(SyncAllFollowedShows::class.java)
            .addTag(SyncAllFollowedShows.TAG)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresDeviceIdle(true)
                    .build()
            )
            .build()
        workManager.enqueue(request)
    }

    override fun setupNightSyncs() {
        val request = PeriodicWorkRequest.Builder(
            SyncAllFollowedShows::class.java,
            24L,
            TimeUnit.HOURS,
            12L,
            TimeUnit.HOURS
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresDeviceIdle(true)
                .build()
        ).build()

        workManager.enqueueUniquePeriodicWork(
            SyncAllFollowedShows.NIGHTLY_SYNC_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

}