package com.tanya.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tanya.common.ui.view.util.ObservableLoadingCounter
import com.tanya.common.ui.view.util.ShowStateSelector
import com.tanya.data.entities.SortOption
import com.tanya.data.results.FollowedShowEntryWithShow
import com.tanya.domain.interactors.ChangeShowFollowStatus
import com.tanya.domain.interactors.UpdateFollowedShows
import com.tanya.domain.observers.ObservePagedFollowedShows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LibraryViewModel @Inject constructor(
    private val updateFollowedShows: UpdateFollowedShows,
    private val observePagedFollowedShows: ObservePagedFollowedShows,
    private val changeShowFollowStatus: ChangeShowFollowStatus,
    private val dataStore: LibarayDataStore
): ViewModel() {

    private val pendingActions = MutableSharedFlow<LibraryAction>()

    private val loadingState = ObservableLoadingCounter()
    private val showSelection = ShowStateSelector()

    val pagedList: Flow<PagingData<FollowedShowEntryWithShow>> =
        observePagedFollowedShows.flow.cachedIn(viewModelScope)

    private val availableSorts = listOf(
        SortOption.SUPER_SORT,
        SortOption.LAST_WATCHED,
        SortOption.ALPHABETICAL,
        SortOption.DATE_ADDED
    )

    //private val sort = MutableStateFlow(SortOption.SUPER_SORT)
    private val sort = dataStore.sortOption

    val state: StateFlow<LibraryViewState> = combine(
        loadingState.observable,
        showSelection.observeSelectedShowIds(),
        showSelection.observeIsSelectionOpen(),
        sort
    ) { loading, selectedShowIds, isSelectionOpen, sort ->
        LibraryViewState(
            isLoading = loading,
            selectionOpen = isSelectionOpen,
            selectedShowIds = selectedShowIds,
            availableSorts = availableSorts,
            sort = sort
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = LibraryViewState.Empty
    )

    init {

        viewModelScope.launch {
            sort.collect { updateDataSource() }
        }

        viewModelScope.launch {
            pendingActions.collect {
                when (it) {
                    LibraryAction.RefreshAction -> {}
                    is LibraryAction.ChangeSort -> setSort(it.sort)
                    else -> {}
                }
            }
        }

    }

    private suspend fun updateDataSource() {
        observePagedFollowedShows(
            ObservePagedFollowedShows.Params(
                sort = sort.first(),
                pagingConfig = PAGING_CONFIG
            )
        )
    }

    private fun setSort(sort: SortOption) {
        viewModelScope.launch {
            //this@LibraryViewModel.sort.emit(sort)
            dataStore.updateSortOption(sort)
        }
    }

    fun submitAction(action: LibraryAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 16,
            initialLoadSize = 32,
        )
    }
}