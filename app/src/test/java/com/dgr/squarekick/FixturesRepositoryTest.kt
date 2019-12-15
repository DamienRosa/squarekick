package com.dgr.squarekick

import com.dgr.squarekick.data.db.SquareKickDataBase
import com.dgr.squarekick.data.network.ApiFootballAPI
import com.dgr.squarekick.data.network.responses.fixtures.AwayTeam
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.fixtures.FixturesResponse
import com.dgr.squarekick.data.network.responses.fixtures.HomeTeam
import com.dgr.squarekick.data.network.responses.leagues.Api
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.data.network.responses.leagues.LeaguesResponse
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class FixturesRepositoryTest {

    @Mock
    lateinit var mockApiFootballAPI: ApiFootballAPI

    @Mock
    lateinit var mockSquareKickDataBase: SquareKickDataBase

    lateinit var fixturesRepository: FixturesRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCorountineRule = MainCoroutineRules()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        mainCorountineRule.runBlockingTest {
            whenever(mockApiFootballAPI.getCompetitionsPerCountry()).thenReturn(
                Response.success(mockedResponseLeague)
            )
            whenever(mockApiFootballAPI.getFixtureById(1)).thenReturn(
                Response.success(mockedResponseFixtures)
            )
            whenever(mockApiFootballAPI.getFixturesByDate("2019-12-12")).thenReturn(
                Response.success(mockedResponseFixtures)
            )
        }
        fixturesRepository = FixturesRepository(mockApiFootballAPI, mockSquareKickDataBase)
    }

    @Test
    fun testGetLeagues_receiveOneItem() {
        mainCorountineRule.runBlockingTest {
            val testObserver = fixturesRepository.fetchLeagues()
            assertThat(testObserver.api.leagues, IsEqual(listOf(mockedLeague)))
        }
    }

    @Test
    fun testGetFixturesById_receiveOneItem() {
        mainCorountineRule.runBlockingTest {
            val testObserver = fixturesRepository.fetchFixtureById(1)
            assertThat(testObserver.api.fixtures, IsEqual(listOf(mockedFixture)))
        }
    }

    @Test
    fun testGetFixturesByDate_receiveOneItem() {
        mainCorountineRule.runBlockingTest {
            val testObserver = fixturesRepository.fetchFixturesDate("2019-12-12")
            assertThat(testObserver.api.fixtures, IsEqual(listOf(mockedFixture)))
        }
    }

    companion object {
        private val mockedLeague =
            Leagues(
                123,
                "League Name",
                "Country Name",
                "111",
                1,
                "1",
                "1",
                "logo",
                null,
                0,
                1
            )

        private val mockedFixture =
            Fixtures(
                1,
                123,
                null,
                123123123,
                0,
                0,
                null,
                null,
                null,
                0,
                null,
                null,
                HomeTeam(1, null, null),
                AwayTeam(2, null, null),
                0,
                0,
                null
                )

        private val mockedResponseLeague =
            LeaguesResponse(Api(1, listOf(mockedLeague)))

        private val mockedResponseFixtures =
            FixturesResponse(
                com.dgr.squarekick.data.network.responses.fixtures.Api(
                    1,
                    listOf(mockedFixture)
                )
            )
    }
}