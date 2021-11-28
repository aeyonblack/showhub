package com.tanya.tasks

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.tanya.domain.interactors.UpdateShowSeasonData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncShowWatchedProgress @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val updateShowSeasonData: UpdateShowSeasonData
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val showId = inputData.getLong(PARAM_SHOW_ID, -1)
        updateShowSeasonData.executeSync(UpdateShowSeasonData.Params(showId, true))
        return Result.success()
    }

    companion object {
        const val TAG = "sync-show-watched-episodes"
        private const val PARAM_SHOW_ID = "show-id"

        fun buildData(showId: Long) =
            Data.Builder()
                .putLong(PARAM_SHOW_ID, showId)
                .build()
    }
}