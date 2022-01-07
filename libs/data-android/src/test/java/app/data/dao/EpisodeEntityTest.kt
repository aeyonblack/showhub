package app.data.dao

import app.DatabaseTest
import app.util.insertShow
import app.util.s1
import app.util.s1e1
import com.tanya.data.ShowhubDatabase
import com.tanya.data.android.repository.database.DatabaseModuleBinds
import com.tanya.data.daos.EpisodesDao
import com.tanya.data.daos.SeasonsDao
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
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


  /*  @Nested
    @DisplayName("When one episode is inserted into the database")
    inner class EpisodeInsertedIntoDatabase {

        @BeforeEach
        fun setup() {
            runBlocking {
                episodesDao.insert(s1e1)
            }
        }

        @Test
        @DisplayName("quering the database with the id of " +
                "the inserted episode returns that episode")
        fun databaseHasOneEpisodeWithId() = testScope.runBlockingTest {
            episodesDao.insert(s1e1)
            assertThat(episodesDao.episodeWithId(s1e1.id)).isEqualTo(s1e1)
        }

        @Test
        @DisplayName("inserting another episode with a dublicate " +
                "trakt id throws a SQLiteConstraintException error")
        fun insertingEpisodeWithDuplicateTraktId() = testScope.runBlockingTest {
            assertThrows<SQLiteConstraintException> { episodesDao.insert(s1e1.copy(id = 0)) }
        }

        @Test
        @DisplayName("the database is empty when the episode is deleted")
        fun databaseEmptyOnDelete() = testScope.runBlockingTest {
            episodesDao.delete(s1e1)
            assertThat(episodesDao.episodeWithId(s1e1.id)).isNull()
        }

        @Test
        @DisplayName("deleting the season with the episode deletes the episode as well")
        fun deletingShowSeaonWithEpisode() = testScope.runBlockingTest {
            seasonsDao.delete(s1)
            assertThat(episodesDao.episodeWithId(s1e1.id)).isNull()
        }

        @Test
        @DisplayName("the show id for the current episode id " +
                "is the id of the current show in the database")
        fun showIdForEpisodeId() = testScope.runBlockingTest {
            assertThat(episodesDao.showIdForEpisodeId(s1e1.id)).isEqualTo(showId)
        }

    }

    @Nested
    @DisplayName("When multiple episodes are added to the database")
    inner class NextEpisodeAfterCurrent {

        @BeforeEach
        fun setup() = runBlocking {
            episodesDao.insertAll(s1_episodes)
        }

        @Test
        @DisplayName("each episode is aired in the correct sequence")
        fun nextEpisodeForShowId() = testScope.runBlockingTest {

            val softly = SoftAssertions()

            softly.assertThat(
                episodesDao.observeNextEpisodeForShowAfter(
                    showId = showId,
                    seasonNumber = 0,
                    episodeNumber = 0
                ).first()?.episode
            ).isEqualTo(s1e1)

            softly.assertThat(
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
            ).isNull()

            softly.assertAll()

        }

    }*/

    @After
    fun tearDown() {
        testScope.cleanupTestCoroutines()
    }

}