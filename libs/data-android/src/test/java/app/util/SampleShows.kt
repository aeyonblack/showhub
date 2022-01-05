package app.util

import com.tanya.data.ShowhubDatabase
import com.tanya.data.entities.EpisodeEntity
import com.tanya.data.entities.SeasonEntity
import com.tanya.data.entities.ShowEntity
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

/*sample mock show ids*/
const val showId = 1L
const val showId2 = 2L
const val showId3 = 3L

/*sample mock shows*/
val show = ShowEntity(id = showId, title = "The Theory of Everything", traktId = 133)
val show2 = ShowEntity(id = showId2, title = "A Brief History of Time", traktId = 1293)
val show3 = ShowEntity(id = showId3, title = "Lost in Space", traktId = 327)

/*mock functions for deleting and inserting a show into the database*/
internal suspend fun insertShow(db: ShowhubDatabase) = db.showDao().insert(show)
internal suspend fun deleteShow(db: ShowhubDatabase) = db.showDao().delete(show)

/*sample show season ids for show (1)*/
const val s1_id = 1L
const val s2_id = 2L
const val s0_id = 3L

/*sample seasons*/
val s1 = SeasonEntity(
    id = s1_id,
    showId = showId,
    title = "Season 1",
    number = 1,
    traktId = 773
)
val s2 = SeasonEntity(
    id = s2_id,
    showId = showId,
    title = "Season 2",
    number = 2,
    traktId = 782
)
val s0 = SeasonEntity(
    id = s0_id,
    showId = showId,
    title = "Specials",
    number = SeasonEntity.NUMBER_SPECIALS,
    traktId = 2843
)

/*episodes air date*/
private val firstAired = OffsetDateTime.of(2017, 6, 18, 19, 30, 0, 0, ZoneOffset.UTC)

/*sample mock episodes for season 1*/
val s1e1 = EpisodeEntity(
    id = 1,
    title = "Big Bang",
    seasonId = s1.id,
    number = 0,
    traktId = 3133,
    firstAired = firstAired
)
val s1e2 = EpisodeEntity(
    id = 2,
    title = "Wrinkle in Time",
    seasonId = s1.id,
    number = 1,
    traktId = 65522,
    firstAired = firstAired.plusWeeks(1)
)
val s1e3 = EpisodeEntity(
    id = 3,
    title = "Space-time Warp",
    seasonId = s1.id,
    number = 2,
    traktId = 3133,
    firstAired = firstAired.plusWeeks(2)
)

/*sample mock up episodes for season 2*/
val s2e1 = EpisodeEntity(
    id = 4,
    title = "Hubble",
    seasonId = s2.id,
    number = 0,
    traktId = 438537,
    firstAired = firstAired.plusWeeks(52)
)
val s2e2 = EpisodeEntity(
    id = 5,
    title = "James-webb",
    seasonId = s2.id,
    number = 1,
    traktId = 23463,
    firstAired = firstAired.plusWeeks(53)
)
val s2e3 = EpisodeEntity(
    id = 6,
    title = "Black holes",
    seasonId = s2.id,
    number = 2,
    traktId = 483543,
    firstAired = firstAired.plusWeeks(54)
)
