package com.tanya.data.daos

import com.tanya.data.MultipleEntry
import com.tanya.data.results.EntryWithShow
import kotlinx.coroutines.flow.Flow

abstract class PairEntryDao<EC: MultipleEntry, LI: EntryWithShow<EC>> : EntityDao<EC>() {
    abstract fun entries(showId: Long): List<EC>
    abstract fun entriesWithShows(showId: Long): List<LI>
    abstract fun entriesWithShowsObservable(showId: Long): Flow<List<LI>>
    abstract suspend fun deleteWithShowId(showId: Long)
}