package com.tanya.data.repositories.shows

import com.tanya.data.entities.ShowEntity

fun mergeShows(
    local: ShowEntity = ShowEntity.EMPTY_SHOW,
    trakt: ShowEntity = ShowEntity.EMPTY_SHOW,
    tmdb: ShowEntity = ShowEntity.EMPTY_SHOW
) = local.copy(

)