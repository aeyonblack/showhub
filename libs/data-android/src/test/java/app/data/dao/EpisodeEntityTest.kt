package app.data.dao

import android.database.sqlite.SQLiteConstraintException
import app.DatabaseTest
import app.util.insertShow
import app.util.s1
import app.util.s1_episodes
import app.util.s1e1
import app.util.s1e2
import app.util.showId
import com.tanya.data.ShowhubDatabase
import com.tanya.data.android.repository.database.DatabaseModuleBinds
import com.tanya.data.daos.EpisodesDao
import com.tanya.data.daos.SeasonsDao
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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
        Dispatchers.setMain(StandardTestDispatcher())
        hiltRule.inject()
        runBlocking {
            insertShow(database)
            seasonsDao.insert(s1)
        }
    }

    @Test
    fun databaseHasOneEpisodeWithId() = runTest {
        episodesDao.insert(s1e1)
        assertThat(episodesDao.episodeWithId(s1e1.id)).isEqualTo(s1e1)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertingEpisodeWithDuplicateTraktId() = runTest {
        episodesDao.insert(s1e1)
        episodesDao.insert(s1e1.copy(id = 0))
    }

    @Test
    fun databaseEmptyOnDelete() = runTest {
        episodesDao.insert(s1e1)
        episodesDao.delete(s1e1)
        assertThat(episodesDao.episodeWithId(s1e1.id)).isNull()
    }

    @Test
    fun deletingShowSeaonWithEpisode() = runTest {
        episodesDao.insert(s1e1)
        seasonsDao.delete(s1)
        assertThat(episodesDao.episodeWithId(s1e1.id)).isNull()
    }

    @Test
    fun showIdForEpisodeId() = runTest {
        episodesDao.insert(s1e1)
        assertThat(episodesDao.showIdForEpisodeId(s1e1.id)).isEqualTo(showId)
    }

    @Test
    @Ignore("Problem with test case at the moment")
    fun nextEpisodeForShowId() = runTest {

        episodesDao.insertAll(s1_episodes)

        val softly = SoftAssertions()

        softly.assertThat(
            episodesDao.observeNextEpisodeForShowAfter(
                showId = showId,
                seasonNumber = 1,
                episodeNumber = 1
            ).first()?.episode
        ).isEqualTo(s1e2)

        softly.assertAll()

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}