package com.tanya.data.entities

import androidx.room.*
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "episodes",
    indices = [
        Index(value = ["trakt_id"], unique = true),
        Index(value = ["season_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = SeasonEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("season_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
    @ColumnInfo(name = "season_id") val seasonId: Long,
    @ColumnInfo(name = "trakt_id") override val traktId: Int? = null,
    @ColumnInfo(name = "tmdb_id") override val tmdbId: Int? = null,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "overview") val summary: String? = null,
    @ColumnInfo(name = "number") val number: Int? = null,
    @ColumnInfo(name = "first_aired") val firstAired: OffsetDateTime? = null,
    @ColumnInfo(name = "trakt_rating") val traktRating: Float? = null,
    @ColumnInfo(name = "trakt_rating_votes") val traktRatingVotes: Int? = null,
    @ColumnInfo(name = "tmdb_backdrop_path") val tmdbBackdropPath: String? = null
): BaseEntity, TmdbIdEntity, TraktIdEntity {
    companion object {
        val empty = EpisodeEntity(seasonId = 0)
    }

    @delegate:Ignore
    val isAired by lazy { firstAired?.isBefore(OffsetDateTime.now()) ?: false }
}