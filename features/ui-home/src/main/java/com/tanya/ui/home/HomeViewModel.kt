package com.tanya.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.common.ui.view.util.ObservableLoadingCounter
import com.tanya.domain.observers.ObservePopularShows
import com.tanya.domain.observers.ObserveTrendingShows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    observePopularShows: ObservePopularShows,
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
    }
}