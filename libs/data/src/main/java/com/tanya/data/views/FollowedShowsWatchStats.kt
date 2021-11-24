package com.tanya.data.views

import androidx.room.DatabaseView
import com.tanya.data.entities.SeasonEntity

@DatabaseView(
    value =
    """
        SELECT fs.id, COUNT(*) as episodeCount, COUNT(w.watched_at) as watchedEpisodeCount
        FROM library as fs
        INNER JOIN seasons AS s ON fs.show_id = s.show_id
        INNER JOIN episodes AS eps ON eps.season_id = s.id
        LEFT JOIN watched_episodes as w ON w.episode_id = eps.id
        WHERE eps.first_aired IS NOT NULL
        AND datetime(eps.first_aired) < datetime('now')
        AND s.number != ${SeasonEntity.NUMBER_SPECIALS}
        AND s.ignored = 0
        GROUP BY fs.id
    """
)
data class FollowedShowsWatchStats(
    val id: Long,
    val episodeCount: Int,
    val watchedEpisodeCount: Int
)