package com.tanya.data.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get

/**
 * Fetch store items from the cache or network depending on whether
 * we want fresh data (fetch from network) or cached data (fetch from disk)
 * [forceFresh] = true, when refreshing. In that case,we want fresh data
 */
suspend inline fun <Key : Any, Output : Any> Store<Key, Output>.fetch(
    key: Key,
    forceFresh: Boolean = false
): Output = when {
    // If we're forcing a fresh fetch, do it now
    forceFresh -> fresh(key)
    else -> get(key)
}

/**
 * Filter out Loading data and null data,
 * Only return a flow containing relevant data dispatched by the store
 */

fun <T> Flow<StoreReadResponse<T>>.filterForResult(): Flow<StoreReadResponse<T>> = filterNot {
    it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData
}