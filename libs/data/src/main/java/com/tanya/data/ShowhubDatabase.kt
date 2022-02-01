package com.tanya.data

import com.tanya.data.daos.*

interface ShowhubDatabase {
    fun showDao(): ShowDao
    fun showImagesDao(): ShowImagesDao
    fun popularDao(): PopularDao
    fun trendingDao(): TrendingDao
    fun relatedShowsDao(): RelatedShowsDao
    fun episodesDao(): EpisodesDao
    fun episodeWatchDao(): EpisodeWatchEntityDao
    fun followedShowsDao(): FollowedShowsDao
    fun seasonDao(): SeasonsDao
    fun showFtsDao(): ShowFtsDao
}