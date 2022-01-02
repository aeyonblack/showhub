package com.tanya.ui.library

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tanya.data.entities.SortOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class LibarayDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val sortOption: Flow<SortOption> = context.dataStore.data.map {
        when (it[SORT_OPTION_KEY]) {
            SortOption.LAST_WATCHED.sort -> SortOption.LAST_WATCHED
            SortOption.DATE_ADDED.sort -> SortOption.DATE_ADDED
            SortOption.ALPHABETICAL.sort -> SortOption.ALPHABETICAL
            else -> SortOption.SUPER_SORT
        }
    }

    val layout: Flow<LayoutType> = context.dataStore.data.map {
        when (it[LAYOUT_KEY]) {
            LayoutType.GRID.type -> LayoutType.GRID
            else -> LayoutType.LIST
        }
    }

    suspend fun updateSortOption(sort: SortOption) {
        context.dataStore.edit {
            if (it[SORT_OPTION_KEY] != sort.sort) {
                it[SORT_OPTION_KEY] = sort.sort
            }
        }
    }

    suspend fun updateLayout(layout: LayoutType) {
        context.dataStore.edit {
            if (it[LAYOUT_KEY] != layout.type) {
                it[LAYOUT_KEY] = layout.type
            }
        }
    }

    companion object {
        private val SORT_OPTION_KEY = stringPreferencesKey("sort_option")
        private val LAYOUT_KEY = stringPreferencesKey("layout_type")
    }
}