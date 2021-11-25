package com.tanya.data.repositories.episodes

import com.tanya.data.DatabaseTransactionRunner
import com.tanya.data.daos.EpisodeWatchEntityDao
import javax.inject.Inject

class EpisodeWatchStore @Inject constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val dao: EpisodeWatchEntityDao
) {
}