package com.tanya.data.entities

import androidx.room.*
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "watched_episodes",
    indices = [
        Index(value = ["trakt_id"], unique = true),
        Index(value = ["episode_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = EpisodeEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("episode_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EpisodeWatchEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
    @ColumnInfo(name = "episode_id") val episodeId: Long,
    @ColumnInfo(name = "trakt_id") val traktId: Long? = null,
    @ColumnInfo(name = "watched_at") val watchedAt: OffsetDateTime,
    @ColumnInfo(name = "pending_action") val pendingAction: PendingAction = PendingAction.NOTHING
): BaseEntity