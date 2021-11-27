package com.tanya.data.results

import androidx.room.Embedded
import androidx.room.Relation
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.SeasonEntity
import java.util.*

class SeasonWithEpisodesAndWatches {

    @Embedded
    lateinit var season: SeasonEntity

    @Relation(
        entity = EpisodeEntity::class,
        parentColumn = "id",
        entityColumn = "season_id",
    )
    var episodes: List<EpisodeWithWatches> = emptyList()

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is SeasonWithEpisodesAndWatches -> season == other.season && episodes == other.episodes
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(season, episodes)
}