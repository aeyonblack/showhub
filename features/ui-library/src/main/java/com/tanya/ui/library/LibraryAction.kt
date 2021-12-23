package com.tanya.ui.library

import com.tanya.data.entities.SortOption

internal sealed class LibraryAction {
    object RefreshAction: LibraryAction()
    data class ChangeSort(val sort: SortOption): LibraryAction()
    data class OpenShowDetails(val showId: Long): LibraryAction()
}