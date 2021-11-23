package com.tanya.data.entities

import androidx.room.*
import com.tanya.data.Entry
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "library",
    indices = [
        Index(value = ["show_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = ShowEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("show_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FollowedShowEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "show_id") override val showId: Long,
    @ColumnInfo(name = "followed_at") val followedAt: OffsetDateTime? = null,
    @ColumnInfo(name = "pending_action") val pendingAction: PendingAction = PendingAction.NOTHING,
    @ColumnInfo(name = "trakt_id") val traktId: Long? = null
): Entry