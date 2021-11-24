package com.tanya.data.views

import androidx.room.DatabaseView
import com.tanya.data.entities.SeasonEntity
import org.threeten.bp.OffsetDateTime

@DatabaseView(
    value = """
        SELECT fs.id, MIN(datetime(eps.first_aired)) AS next_to_watch_air_date
        FROM library AS fs
        INNER JOIN seasons AS s ON fs.show_id = s.show_id
        INNER JOIN episodes AS eps ON eps.season_id = s.id
        LEFT JOIN watched_episodes AS w ON w.episode_id = eps.id
        INNER JOIN followed_last_watched_air_date AS lw ON lw.id = fs.id
        WHERE s.number != ${SeasonEntity.NUMBER_SPECIALS}
        AND s.ignored = 0
        AND watched_at IS NULL
        AND datetime(first_aired) < datetime('now')
        AND datetime(first_aired) > datetime(last_watched_air_date)
        GROUP BY fs.id
    """,
    viewName = "followed_next_to_watch"
)
data class FollowedShowsNextToWatch(
    val id: Long,
    val nextEpisodeToWatchAirDate: OffsetDateTime?
)