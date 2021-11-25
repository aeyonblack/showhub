package com.tanya.data.repositories.episodes

import com.tanya.data.mappers.ShowIdToTraktIdMapper
import javax.inject.Inject

class TraktSeasonsEpisodesDataSource @Inject constructor(
    private val showIdToTraktIdMapper: ShowIdToTraktIdMapper,
) {
}