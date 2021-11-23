package com.tanya.common.ui.view.ui

import androidx.annotation.StringRes
import com.tanya.common.ui.resources.R
import com.tanya.data.entities.Genre

object GenreStringer {
    @StringRes
    fun getLabel(genre: Genre): Int = when (genre) {
        Genre.DRAMA -> R.string.genre_label_drama
        Genre.FANTASY -> R.string.genre_label_fantasy
        Genre.SCIENCE_FICTION -> R.string.genre_label_science_fiction
        Genre.ACTION -> R.string.genre_label_action
        Genre.ADVENTURE -> R.string.genre_label_adventure
        Genre.CRIME -> R.string.genre_label_crime
        Genre.THRILLER -> R.string.genre_label_thriller
        Genre.COMEDY -> R.string.genre_label_comedy
        Genre.HORROR -> R.string.genre_label_horror
        Genre.MYSTERY -> R.string.genre_label_mystery
    }
}