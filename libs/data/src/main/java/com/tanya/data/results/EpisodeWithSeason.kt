package com.tanya.data.results

import androidx.room.Embedded
import androidx.room.Relation
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.SeasonEntity
import java.util.*

@Suppress("PropertyName")
class EpisodeWithSeason {

    @Embedded
    var episode: EpisodeEntity? = null

    @Relation(parentColumn = "season_id", entityColumn = "id")
    var _seasons: List<SeasonEntity> = emptyList()

    val season: SeasonEntity? get() = _seasons.getOrNull(0)

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is EpisodeWithSeason -> episode == other.episode && _seasons == other._seasons
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(episode, _seasons)
}