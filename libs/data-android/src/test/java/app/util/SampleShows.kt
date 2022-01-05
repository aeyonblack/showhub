package app.util

import com.tanya.data.entities.ShowEntity

/*sample mock show ids*/
const val showId1 = 1L
const val showId2 = 2L
const val showId3 = 3L

/*sample mock shows*/
val show = ShowEntity(id = showId1, title = "The Theory of Everything", traktId = 133)
val show2 = ShowEntity(id = showId2, title = "A Brief History of Time", traktId = 1293)
val show3 = ShowEntity(id = showId3, title = "Lost in Space", traktId = 327)