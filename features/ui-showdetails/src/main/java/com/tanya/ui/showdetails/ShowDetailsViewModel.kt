package com.tanya.ui.showdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.base.base.InvokeError
import com.tanya.base.base.InvokeStarted
import com.tanya.base.base.InvokeStatus
import com.tanya.base.base.InvokeSuccess
import com.tanya.common.ui.view.util.ObservableLoadingCounter
import com.tanya.domain.interactors.UpdateRelatedShows
import com.tanya.domain.interactors.UpdateShowDetails
import com.tanya.domain.interactors.UpdateShowImages
import com.tanya.domain.observers.ObserveRelatedShows
import com.tanya.domain.observers.ObserveShowDetails
import com.tanya.domain.observers.ObserveShowImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShowDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateShowDetails: UpdateShowDetails,
    observeShowDetails: ObserveShowDetails,
    private val updateShowImages: UpdateShowImages,
    observeShowImages: ObserveShowImages,
    private val updateRelatedShows: UpdateRelatedShows,
    observeRelatedShows: ObserveRelatedShows
) : ViewModel() {
    private val showId: Long = savedStateHandle.get("showId")!!
    private val loadingState = ObservableLoadingCounter()

    val state = combine(
        loadingState.observable,
        observeShowDetails.flow,
        observeShowImages.flow,
        observeRelatedShows.flow
    ) { refreshing, show, showImages, relatedShows ->
        ShowDetailsViewState(
            show = show,
            backdropImage = showImages.backdrop,
            relatedShows = relatedShows,
            refreshing = refreshing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShowDetailsViewState.EMPTY
    )

    init {
        observeShowDetails(ObserveShowDetails.Params(showId))
        observeShowImages(ObserveShowImages.Params(showId))
        observeRelatedShows(ObserveRelatedShows.Params(showId))

        refresh()
    }

    private fun refresh(forceLoad: Boolean = true) {
        updateShowDetails(UpdateShowDetails.Params(showId, forceLoad)).watchStatus()
        updateShowImages(UpdateShowImages.Params(showId, forceLoad)).watchStatus()
        updateRelatedShows(UpdateRelatedShows.Params(showId, forceLoad)).watchStatus()
    }

    private fun Flow<InvokeStatus>.watchStatus() = viewModelScope.launch { collectStatus() }

    private suspend fun Flow<InvokeStatus>.collectStatus() = collect {
        when(it) {
            InvokeStarted -> loadingState.addLoader()
            InvokeSuccess -> loadingState.removeLoader()
            is InvokeError -> {
                loadingState.removeLoader()
            }
        }
    }
}