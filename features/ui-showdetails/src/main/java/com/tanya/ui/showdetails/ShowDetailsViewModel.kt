package com.tanya.ui.showdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.common.ui.view.util.ObservableLoadingCounter
import com.tanya.domain.interactors.UpdateShowDetails
import com.tanya.domain.interactors.UpdateShowImages
import com.tanya.domain.observers.ObserveShowDetails
import com.tanya.domain.observers.ObserveShowImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShowDetailsViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val updateShowDetails: UpdateShowDetails,
    observeShowDetails: ObserveShowDetails,
    private val updateShowImages: UpdateShowImages,
    observeShowImages: ObserveShowImages
) : ViewModel() {
    private val showId: Long = savedStateHandle.get("showId")!!
    private val loadingState = ObservableLoadingCounter()

    val state = combine(
        loadingState.observable,
        observeShowDetails.flow,
        observeShowImages.flow
    ) { refreshing, show, showImages ->
        ShowDetailsViewState(
            show = show,
            backdropImage = showImages.backdrop,
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

        refresh(false)
    }

    private fun refresh(forceLoad: Boolean) {
        updateShowDetails(UpdateShowDetails.Params(showId, forceLoad))
        updateShowImages(UpdateShowImages.Params(showId, forceLoad))
    }
}