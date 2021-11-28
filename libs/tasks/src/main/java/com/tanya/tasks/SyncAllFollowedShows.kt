package com.tanya.tasks

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tanya.data.entities.RefreshType
import com.tanya.domain.interactors.UpdateFollowedShows
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncAllFollowedShows @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updateFollowedShows: UpdateFollowedShows
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        updateFollowedShows.executeSync(UpdateFollowedShows.Params(true, RefreshType.FULL))
        return Result.success()
    }

    companion object {
        const val TAG = "sync-all-followed-shows"
        const val NIGHTLY_SYNC_TAG = "nightly-sync-all-followed-shows"
    }
}