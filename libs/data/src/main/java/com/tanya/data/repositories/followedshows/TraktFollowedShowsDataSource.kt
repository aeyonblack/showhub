package com.tanya.data.repositories.followedshows

import com.uwetrottmann.trakt5.entities.*
import com.uwetrottmann.trakt5.enums.Extended
import com.uwetrottmann.trakt5.enums.ListPrivacy
import com.uwetrottmann.trakt5.services.Users
import javax.inject.Inject
import javax.inject.Provider

class TraktFollowedShowsDataSource @Inject constructor(
    private val usersService: Provider<Users>,
    listEntryToShowMapper: TraktListEntryToTiviShow,
    listEntryToFollowedEntry: TraktListEntryToFollowedShowEntry
) : FollowedShowsDataSource {
    companion object {
        private val LIST_NAME = "Following"
    }

    private val listShowsMapper = pairMapperOf(listEntryToFollowedEntry, listEntryToShowMapper)

    override suspend fun addShowIdsToList(listId: Int, shows: List<TiviShow>): Result<Unit> {
        val syncItems = SyncItems()
        syncItems.shows = shows.map { show ->
            SyncShow().apply {
                ids = ShowIds().apply {
                    trakt = show.traktId
                    imdb = show.imdbId
                    tmdb = show.tmdbId
                }
            }
        }
        return usersService.get().addListItems(UserSlug.ME, listId.toString(), syncItems)
            .executeWithRetry()
            .toResultUnit()
    }

    override suspend fun removeShowIdsFromList(listId: Int, shows: List<TiviShow>): Result<Unit> {
        val syncItems = SyncItems()
        syncItems.shows = shows.map { show ->
            SyncShow().apply {
                ids = ShowIds().apply {
                    trakt = show.traktId
                    imdb = show.imdbId
                    tmdb = show.tmdbId
                }
            }
        }
        return usersService.get().deleteListItems(UserSlug.ME, listId.toString(), syncItems)
            .executeWithRetry()
            .toResultUnit()
    }

    override suspend fun getListShows(listId: Int): Result<List<Pair<FollowedShowEntry, TiviShow>>> {
        return usersService.get().listItems(UserSlug.ME, listId.toString(), Extended.NOSEASONS)
            .executeWithRetry()
            .toResult(listShowsMapper)
    }

    override suspend fun getFollowedListId(): Result<Int> {
        val fetchResult: Result<Int>? = try {
            usersService.get().lists(UserSlug.ME)
                .executeWithRetry()
                .bodyOrThrow()
                .firstOrNull { it.name == LIST_NAME }
                ?.let { Success(it.ids.trakt) }
        } catch (t: Throwable) {
            ErrorResult(t)
        }

        if (fetchResult is Success) {
            return fetchResult
        }

        return try {
            usersService.get().createList(
                UserSlug.ME,
                TraktList().name(LIST_NAME).privacy(ListPrivacy.PRIVATE)
            )
                .executeWithRetry()
                .bodyOrThrow()
                .let { Success(it.ids.trakt) }
        } catch (t: Throwable) {
            ErrorResult(t)
        }
    }
}