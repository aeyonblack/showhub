package com.tanya.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

/**
 * Room database entity for saving shows
 */
@Entity(
    tableName = "shows",
    indices = [
        Index(value = ["trakt_id"], unique = true),
        Index(value = ["tmdb_id"])
    ]
)
data class ShowEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") override val id: Long = 0,
    @ColumnInfo(name = "trakt_id") override val traktId: Int? = null,
    @ColumnInfo(name = "tmdb_id") override val tmdbId: Int? = null,
    @ColumnInfo(name = "imdb_id") val imdbId: Int? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "original_title") val originalTitle: String? = null,
    @ColumnInfo(name = "overview") val summary: String? = null,
    @ColumnInfo(name = "homepage") val homepage: String? = null,
    @ColumnInfo(name = "trakt_rating") val traktRating: Float? = null,
    @ColumnInfo(name = "trakt_votes") val traktVotes: Int? = null,
    @ColumnInfo(name = "certification") val certification: String? = null,
    @ColumnInfo(name = "first_aired") val firstAired: OffsetDateTime? = null,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "network") val network: String? = null,
    @ColumnInfo(name = "network_logo_path") val networkLogoPath: String? = null,
    @ColumnInfo(name = "runtime") val runtime: Int? = null,
    @ColumnInfo(name = "genres") val _genres: String? = null,
    @ColumnInfo(name = "last_trakt_data_update") val traktDataUpdate: OffsetDateTime? = null,
    @ColumnInfo(name = "status") val status: ShowStatus? = null,
    @ColumnInfo(name = "airs_day") val airsDay: DayOfWeek? = null,
    @ColumnInfo(name = "airs_time") val airsTime: LocalTime? = null,
    @ColumnInfo(name = "airs_tz") val airsTimeZoneId: ZoneId? = null
): BaseEntity, TraktIdEntity, TmdbIdEntity