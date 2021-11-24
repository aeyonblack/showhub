package com.tanya.data.results

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.EpisodeWatchEntity
import com.tanya.data.entities.PendingAction
import java.util.*

class EpisodeWithWatches {
    @Embedded
    lateinit var episode: EpisodeEntity

    @Relation(parentColumn = "id", entityColumn = "episode_id")
    lateinit var watches: List<EpisodeWatchEntity>

    @delegate:Ignore
    val hasWatches by lazy { watches.isNotEmpty() }

    @delegate:Ignore
    val isWatched by lazy {
        watches.any { it.pendingAction != PendingAction.DELETE }
    }

    @delegate:Ignore
    val hasPending by lazy {
        watches.any { it.pendingAction != PendingAction.NOTHING }
    }

    @delegate:Ignore
    val onlyPendingDeletes by lazy {
        watches.all { it.pendingAction == PendingAction.DELETE }
    }

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is EpisodeWithWatches -> watches == other.watches && episode == other.episode
        else -> false
    }

    override fun hashCode(): Int = Objects.hash(episode, watches)
}