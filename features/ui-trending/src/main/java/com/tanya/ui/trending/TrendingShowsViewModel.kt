package com.tanya.ui.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tanya.domain.observers.ObservePagedTrendingShows
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendingShowsViewModel @Inject constructor(
    observePagedTrendingShows: ObservePagedTrendingShows
) : ViewModel() {

    val pagedList = observePagedTrendingShows.flow.cachedIn(viewModelScope)

    init {
        observePagedTrendingShows(ObservePagedTrendingShows.Params(PAGING_CONFIG))
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 60,
            initialLoadSize = 60
        )
    }

}