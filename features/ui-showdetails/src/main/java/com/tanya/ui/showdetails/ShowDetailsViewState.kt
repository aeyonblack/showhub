package com.tanya.ui.showdetails

import androidx.compose.runtime.Immutable
import com.tanya.data.entities.ShowEntity
import com.tanya.data.entities.ShowImagesEntity

@Immutable
internal data class ShowDetailsViewState(
    val show: ShowEntity = ShowEntity.EMPTY_SHOW,
    val backdropImage: ShowImagesEntity? = null,
    val refreshing: Boolean = false
) {
    companion object {
        val EMPTY = ShowDetailsViewState()
    }
}