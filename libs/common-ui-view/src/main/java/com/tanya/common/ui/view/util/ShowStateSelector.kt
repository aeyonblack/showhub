package com.tanya.common.ui.view.util

import com.tanya.data.entities.ShowEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShowStateSelector {
    private val selectedShowIds = MutableStateFlow<Set<Long>>(emptySet())
    private val isSelectionOpen = MutableStateFlow(false)

    fun observeSelectedShowIds(): StateFlow<Set<Long>> = selectedShowIds.asStateFlow()

    fun observeIsSelectionOpen(): StateFlow<Boolean> = isSelectionOpen.asStateFlow()

    fun getSelectedShowIds(): Set<Long> = selectedShowIds.value

    fun onItemClick(show: ShowEntity): Boolean {
        if (isSelectionOpen.value) {
            val selectedIds = selectedShowIds.value
            val newSelection = when (show.id) {
                in selectedIds -> selectedIds - show.id
                else -> selectedIds + show.id
            }
            isSelectionOpen.value = newSelection.isNotEmpty()
            selectedShowIds.value = newSelection
            return true
        }
        return false
    }

    fun onItemLongClick(show: ShowEntity): Boolean {
        if (!isSelectionOpen.value) {
            isSelectionOpen.value = true

            val newSelection = selectedShowIds.value + show.id
            isSelectionOpen.value = newSelection.isNotEmpty()
            selectedShowIds.value = newSelection
            return true
        }
        return false
    }

    fun clearSelection() {
        selectedShowIds.value = emptySet()
        isSelectionOpen.value = false
    }
}