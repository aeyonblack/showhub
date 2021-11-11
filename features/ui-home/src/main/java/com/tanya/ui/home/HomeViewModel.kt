package com.tanya.ui.home

import androidx.lifecycle.ViewModel
import com.tanya.domain.observers.ObservePopularShows
import com.tanya.domain.observers.ObserveTrendingShows
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    observePopularShows: ObservePopularShows,
    observeTrendingShows: ObserveTrendingShows
): ViewModel() {
}