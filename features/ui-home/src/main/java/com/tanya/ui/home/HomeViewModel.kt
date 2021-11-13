package com.tanya.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.common.ui.view.util.ObservableLoadingCounter
import com.tanya.common.ui.view.util.collectInto
import com.tanya.domain.interactors.UpdatePopularShows
import com.tanya.domain.interactors.UpdateTrendingShows
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
    observeTrendingShows: ObserveTrendingShows
): ViewModel() {
    private val trendingLoadingState = ObservableLoadingCounter()
    private val popularLoadingState = ObservableLoadingCounter()

    private val pendingActions = MutableSharedFlow<HomeAction>()

    val state: StateFlow<HomeViewState> = combine(
        trendingLoadingState.observable,
        popularLoadingState.observable,
        observePopularShows.flow,
        observeTrendingShows.flow
    ) { trendingLoad, popularLoad, popular,trending ->
        Log.d("homeViewModel", "Trending shows ${trending.size}")
        Log.d("homeViewModel", "Popular shows ${popular.size}")
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
        observeTrendingShows(ObserveTrendingShows.Params(10))
        observePopularShows(ObservePopularShows.Params(10))

        viewModelScope.launch {
            refresh(true)
        }
    }

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
}