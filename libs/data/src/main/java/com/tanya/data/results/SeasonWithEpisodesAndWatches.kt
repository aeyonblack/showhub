package com.tanya.data.results

import androidx.room.Embedded
import com.tanya.data.entities.SeasonEntity

class SeasonWithEpisodesAndWatches {

    @Embedded
    lateinit var season: SeasonEntity


}