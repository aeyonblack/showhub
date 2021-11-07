package com.tanya.data

import com.tanya.data.entities.BaseEntity

interface Entry : BaseEntity {
    val showId: Long
}

interface MultipleEntry : Entry {
    val otherShowId: Long
}

interface PaginatedEntry : Entry {
    val page: Int
}