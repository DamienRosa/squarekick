package com.dgr.squarekick.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dgr.squarekick.MainCoroutineRules
import com.dgr.squarekick.data.network.responses.fixtures.AwayTeam
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.fixtures.HomeTeam
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.ui.home.HomeViewModel
import com.dgr.squarekick.ui.home.LeaguesFixturesItem
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @Mock
    lateinit var mockFixturesRepository: FixturesRepository

    lateinit var homeViewModel: HomeViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRules()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        MockitoAnnotations.initMocks(this)

        mainCoroutineRule.runBlockingTest {
            whenever(mockFixturesRepository.fetchLeaguesByFixtures("2019-12-12"))
                .thenReturn(mockedMapLeaguesFixtures)
        }

        homeViewModel = HomeViewModel(mockFixturesRepository)
    }

    @Test
    fun testGetLeaguesFixturesByDate_success() {
        homeViewModel.fetchLeaguesFixturesByDate("2019-12-12")

        val leagueFixtures = homeViewModel.leagueFixtures

        val observer = mock(Observer::class.java) as Observer<List<LeaguesFixturesItem>>

        leagueFixtures.observeForever(observer)
        assertThat(
            leagueFixtures.value as List<LeaguesFixturesItem>,
            not(nullValue())
        )

        val league = leagueFixtures.value?.first()
        assertThat(league?.league?.league, IsEqual(mockedLeague))
        assertThat(league?.league?.activeFixtures, IsEqual(0))
        assertThat(league?.league?.totalFixtures, IsEqual(1))
    }

    @Test
    fun testGetLeaguesFixturesByDate_fail() {
        homeViewModel.fetchLeaguesFixturesByDate("2019-12-10")

        val observer = mock(Observer::class.java) as Observer<Boolean>

        val emptyMessage = homeViewModel.emptyListMessage
        emptyMessage.observeForever(observer)

        assertThat(homeViewModel.leagueFixtures.value, `is`(nullValue()))

        assertThat(emptyMessage.value, IsEqual(true))
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

        private val mockedMapLeaguesFixtures = mapOf(Pair(mockedLeague, listOf(mockedFixture)))
    }
}