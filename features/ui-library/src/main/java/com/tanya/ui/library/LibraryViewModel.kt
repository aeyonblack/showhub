package com.tanya.ui.library

import androidx.lifecycle.ViewModel
import com.tanya.domain.interactors.ChangeShowFollowStatus
import com.tanya.domain.interactors.UpdateFollowedShows
import com.tanya.domain.observers.ObservePagedFollowedShows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val updateFollowedShows: UpdateFollowedShows,
    private val observePagedFollowedShows: ObservePagedFollowedShows,
    private val changeShowFollowStatus: ChangeShowFollowStatus
): ViewModel() {

    private val pendingActions = MutableSharedFlow<LibraryAction>()


    init {

    }
}