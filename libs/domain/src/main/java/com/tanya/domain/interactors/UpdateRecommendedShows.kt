package com.tanya.domain.interactors

import com.tanya.base.util.AppCoroutineDispatchers
import com.tanya.data.android.repository.images.ShowImagesStore
import com.tanya.data.android.repository.recommended.RecommendedShowStore
import com.tanya.data.android.repository.shows.ShowsStore
import javax.inject.Inject


class UpdateRecommendedShows @Inject constructor(
    private val recommendedShowStore: RecommendedShowStore,
    private val showStore: ShowsStore,
    private val showImagesStore: ShowImagesStore,
    private val dispatchers: AppCoroutineDispatchers,
) {
    // TODO: Implement authentication before moving to recommend shows
}