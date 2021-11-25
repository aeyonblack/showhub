package com.tanya.data.mappers

import com.tanya.data.entities.ShowEntity
import com.uwetrottmann.trakt5.entities.ListEntry
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Singleton
class TraktListEntryToTiviShow @Inject constructor(
    private val showMapper: TraktShowToShowEntity
) : Mapper<ListEntry, ShowEntity> {
    override suspend fun map(from: ListEntry) = showMapper.map(from.show)
}