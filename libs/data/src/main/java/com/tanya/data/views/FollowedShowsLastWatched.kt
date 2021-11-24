package com.tanya.data.views

import androidx.room.DatabaseView
import com.tanya.data.entities.SeasonEntity
import org.threeten.bp.OffsetDateTime

@DatabaseView(
    value = """
        SELECT fs.id, MAX(datetime(eps.first_aired)) as last_watched_air_date
        FROM library as fs
        INNER JOIN seasons AS s ON fs.show_id = s.show_id
        INNER JOIN episodes AS eps ON eps.season_id = s.id
        INNER JOIN watched_episodes AS w ON w.episode_id = eps.id
        WHERE s.number != ${SeasonEntity.NUMBER_SPECIALS}
        AND s.ignored = 0
        GROUP BY fs.id
    """,
    viewName = "followed_last_watched_air_date"
)
data class FollowedShowsLastWatched(
    val id: Long,
    val lastWatchedEpisodeAirDate: OffsetDateTime?
)