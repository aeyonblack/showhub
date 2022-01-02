package com.tanya.ui.library

import com.tanya.data.entities.SortOption

internal data class LibraryViewState(
    val isLoading: Boolean = false,
    val selectionOpen: Boolean = false,
    val selectedShowIds: Set<Long> = emptySet(),
    val filterActive: Boolean = false,
    val availableSorts: List<SortOption> = emptyList(),
    val sort: SortOption = SortOption.SUPER_SORT,
    val layout: LayoutType = LayoutType.LIST
) {
    companion object {
        val Empty = LibraryViewState()
    }
}