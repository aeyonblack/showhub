package com.tanya.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tanya.common.ui.view.util.ObservableLoadingCounter
import com.tanya.common.ui.view.util.collectInto
import com.tanya.data.entities.SortOption
import com.tanya.data.results.FollowedShowEntryWithShow
import com.tanya.domain.interactors.UpdatePopularShows
import com.tanya.domain.interactors.UpdateTrendingShows
import com.tanya.domain.observers.ObservePagedFollowedShows
import com.tanya.domain.observers.ObservePopularShows
import com.tanya.domain.observers.ObserveTrendingShows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val updatePopularShows: UpdatePopularShows,
    observePopularShows: ObservePopularShows,
    private val updateTrendingShows: UpdateTrendingShows,
    observeTrendingShows: ObserveTrendingShows,
    observePagedFollowedShows: ObservePagedFollowedShows,
): ViewModel() {
    private val trendingLoadingState = ObservableLoadingCounter()
    private val popularLoadingState = ObservableLoadingCounter()

    private val pendingActions = MutableSharedFlow<HomeAction>()

    val pagedFollowedShows: Flow<PagingData<FollowedShowEntryWithShow>> =
        observePagedFollowedShows.flow.cachedIn(viewModelScope)

    val state: StateFlow<HomeViewState> = combine(
        trendingLoadingState.observable,
        popularLoadingState.observable,
        observePopularShows.flow,
        observeTrendingShows.flow
    ) { trendingLoad, popularLoad, popular,trending ->
        HomeViewState(
            trendingItems = trending,
            trendingRefreshing = trendingLoad,
            popularItems = popular,
            popularRefreshing = popularLoad
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeViewState.EMPTY
    )

    init {

        observePagedFollowedShows(
            ObservePagedFollowedShows.Params(
                pagingConfig = PAGING_CONFIG,
                sort = SortOption.LAST_WATCHED
            )
        )

        observeTrendingShows(ObserveTrendingShows.Params(10))
        observePopularShows(ObservePopularShows.Params(10))

        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    HomeAction.RefreshAction -> refresh(true)
                }
            }
        }

        viewModelScope.launch {
            refresh(true)
        }
    }

    @Suppress("SameParameterValue")
    private fun refresh(b: Boolean) {
        viewModelScope.launch {
            updatePopularShows(UpdatePopularShows.Params(UpdatePopularShows.Page.REFRESH, b))
                .collectInto(popularLoadingState)
        }
        viewModelScope.launch {
            updateTrendingShows(UpdateTrendingShows.Params(UpdateTrendingShows.Page.REFRESH, b))
                .collectInto(trendingLoadingState)
        }
    }

    fun submitAction(action: HomeAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 4,
            initialLoadSize = 4,
        )
    }
}