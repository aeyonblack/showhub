package com.tanya.ui.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tanya.domain.observers.ObservePagedPopularShows
import javax.inject.Inject

class PopularShowsViewModel @Inject constructor(
    observePagedPopularShows: ObservePagedPopularShows
) : ViewModel() {

    val pagedList = observePagedPopularShows.flow.cachedIn(viewModelScope)

    init {
        observePagedPopularShows(ObservePagedPopularShows.Params(PAGING_CONFIG))
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 60,
            initialLoadSize = 60
        )
    }

}