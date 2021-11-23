package com.tanya.ui.showdetails

import androidx.compose.runtime.Immutable
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity
import com.tanya.data.results.RelatedShowEntryWithShow

@Immutable
internal data class ShowDetailsViewState(
    val show: ShowEntity = ShowEntity.EMPTY_SHOW,
    val backdropImage: ShowImagesEntity? = null,
    val relatedShows: List<RelatedShowEntryWithShow> = emptyList(),
    val refreshing: Boolean = false
) {
    companion object {
        val EMPTY = ShowDetailsViewState()
    }
}