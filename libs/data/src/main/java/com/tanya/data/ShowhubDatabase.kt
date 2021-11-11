package com.tanya.data

import com.tanya.data.daos.PopularDao
import com.tanya.data.daos.ShowDao
import com.tanya.data.daos.ShowImagesDao
import com.tanya.data.daos.TrendingDao

interface ShowhubDatabase {
    fun showDao(): ShowDao
    fun showImagesDao(): ShowImagesDao
    fun popularDao(): PopularDao
    fun trendingDao(): TrendingDao
}