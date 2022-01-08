package app.data.dao

import android.database.sqlite.SQLiteConstraintException
import app.DatabaseTest
import app.util.*
import com.tanya.data.ShowhubDatabase
import com.tanya.data.android.repository.database.DatabaseModuleBinds
import com.tanya.data.daos.EpisodesDao
import com.tanya.data.daos.SeasonsDao
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(DatabaseModuleBinds::class)
class EpisodeEntityTest : DatabaseTest() {

    @Inject lateinit var database: ShowhubDatabase
    @Inject lateinit var episodesDao: EpisodesDao
    @Inject lateinit var seasonsDao: SeasonsDao

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking {
            insertShow(database)
            seasonsDao.insert(s1)
        }
    }

    @Test
    fun databaseHasOneEpisodeWithId() = testScope.runBlockingTest {
        episodesDao.insert(s1e1)
        assertThat(episodesDao.episodeWithId(s1e1.id)).isEqualTo(s1e1)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertingEpisodeWithDuplicateTraktId() = testScope.runBlockingTest {
        episodesDao.insert(s1e1)
        episodesDao.insert(s1e1.copy(id = 0))
    }

    @Test
    fun databaseEmptyOnDelete() = testScope.runBlockingTest {
        episodesDao.insert(s1e1)
        episodesDao.delete(s1e1)
        assertThat(episodesDao.episodeWithId(s1e1.id)).isNull()
    }

    @Test
    fun deletingShowSeaonWithEpisode() = testScope.runBlockingTest {
        episodesDao.insert(s1e1)
        seasonsDao.delete(s1)
        assertThat(episodesDao.episodeWithId(s1e1.id)).isNull()
    }

    @Test
    fun showIdForEpisodeId() = testScope.runBlockingTest {
        episodesDao.insert(s1e1)
        assertThat(episodesDao.showIdForEpisodeId(s1e1.id)).isEqualTo(showId)
    }

    @Test
    @Ignore("Problem with test case at the moment")
    fun nextEpisodeForShowId() = testScope.runBlockingTest {

        episodesDao.insertAll(s1_episodes)

        val softly = SoftAssertions()

        softly.assertThat(
            episodesDao.observeNextEpisodeForShowAfter(
                showId = showId,
                seasonNumber = 1,
                episodeNumber = 1
            ).first()?.episode
        ).isEqualTo(s1e2)

        /*softly.assertThat(
            episodesDao.observeNextEpisodeForShowAfter(
                showId = showId,
                seasonNumber = 1,
                episodeNumber = 0
            ).first()?.episode
        ).isEqualTo(s1e2)

        softly.assertThat(
            episodesDao.observeNextEpisodeForShowAfter(
                showId = showId,
                seasonNumber = 1,
                episodeNumber = 1
            ).first()?.episode
        ).isEqualTo(s1e3)

        softly.assertThat(
            episodesDao.observeNextEpisodeForShowAfter(
                showId = showId,
                seasonNumber = 1,
                episodeNumber = 2
            ).first()?.episode
        ).isNull()*/

        softly.assertAll()

    }

    @After
    fun tearDown() {
        testScope.cleanupTestCoroutines()
    }

}