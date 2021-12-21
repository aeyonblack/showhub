package com.tanya.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanya.common.ui.view.util.ObservableLoadingCounter
import com.tanya.domain.interactors.SearchShows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val searchShows: SearchShows
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val loadingState = ObservableLoadingCounter()

    private val pendingActions = MutableSharedFlow<SearchAction>()

    val state: StateFlow<SearchViewState> = combine(
        searchShows.flow,
        loadingState.observable
    ) { results, refreshing ->
        SearchViewState(
            searchResults = results,
            refreshing = refreshing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchViewState.Empty
    )

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .collectLatest {
                    val job = launch {
                        loadingState.addLoader()
                        searchShows(SearchShows.Params(it))
                    }
                    job.invokeOnCompletion { loadingState.removeLoader() }
                    job.join()
                }
        }

        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    is SearchAction.Search -> {
                        searchQuery.value = it.searchTerm
                    }
                    else -> {}
                }
            }
        }
    }

    fun submitAction(action: SearchAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

}