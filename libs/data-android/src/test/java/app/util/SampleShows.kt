package app.util

import com.tanya.data.ShowhubDatabase
import com.tanya.data.entities.ShowEntity

/*sample mock show ids*/
const val showId1 = 1L
const val showId2 = 2L
const val showId3 = 3L

/*sample mock shows*/
val show = ShowEntity(id = showId1, title = "The Theory of Everything", traktId = 133)
val show2 = ShowEntity(id = showId2, title = "A Brief History of Time", traktId = 1293)
val show3 = ShowEntity(id = showId3, title = "Lost in Space", traktId = 327)

/*mock functions for deleting and inserting a show into the database*/
internal suspend fun insertShow(db: ShowhubDatabase) = db.showDao().insert(show)
internal suspend fun deleteShow(db: ShowhubDatabase) = db.showDao().delete(show)