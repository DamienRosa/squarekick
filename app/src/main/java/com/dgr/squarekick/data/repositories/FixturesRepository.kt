package com.dgr.squarekick.data.repositories

import com.dgr.squarekick.data.db.SquareKickDataBase
import com.dgr.squarekick.data.db.entites.FixtureEntity
import com.dgr.squarekick.data.db.entites.LeagueEntity
import com.dgr.squarekick.data.network.ApiFootballAPI
import com.dgr.squarekick.data.network.responses.SafeAPIRequest
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.fixtures.FixturesResponse
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.data.network.responses.leagues.LeaguesResponse
import com.dgr.squarekick.utils.Coroutines
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class FixturesRepository(
    private val api: ApiFootballAPI,
    private val db: SquareKickDataBase
) : SafeAPIRequest(), IFixturesRepository {

    override suspend fun fetchLeaguesByFixtures(date: String): Map<Leagues, List<Fixtures>> {
        return coroutineScope {
            val leaguesAsync = async { fetchLeagues() }
            val fixturesAsync = async { fetchFixturesDate(date) }

            val fixturesList = fixturesAsync.await().api
            val leaguesList = leaguesAsync.await().api

            if (leaguesList.results <= 0 || fixturesList.results <= 0) {
                return@coroutineScope emptyMap<Leagues, List<Fixtures>>()
            }

            fixturesList.let { it ->
                it.fixtures
                    .groupBy { leaguesList.leagues.find { _it -> _it.league_id == it.league_id }!! }
                    .toSortedMap(compareBy { it.country })
            }
        }
    }

    override suspend fun fetchFixturesDate(date: String): FixturesResponse {
        return apiRequest {
            api.getFixturesByDate(date)
        }
    }

    override suspend fun fetchLeagues(): LeaguesResponse {
        return apiRequest {
            api.getCompetitionsPerCountry()
        }
    }

    override suspend fun fetchFixtureById(fId: Int) : FixturesResponse {
        return apiRequest {
            api.getFixtureById(fId)
        }
    }

    override fun saveLeagues(leagueList: List<Leagues>?) {
        Coroutines.io {
            val leaguesListDb = leagueList?.map {
                LeagueEntity(
                    it.league_id,
                    it.name,
                    it.country,
                    it.season,
                    it.season_start,
                    it.season_end,
                    it.flag ?: it.logo,
                    it.standings,
                    it.is_current
                )
            }
            db.getLeagueDao().saveAllLeagues(leaguesListDb!!)
        }
    }

    override fun saveFixture(fixtures: List<Fixtures>?) {
        Coroutines.io {
            val fixturesListDb = fixtures?.map {
                FixtureEntity(
                    it.fixture_id,
                    it.league_id,
                    it.event_date,
                    it.event_timestamp,
                    it.firstHalfStart,
                    it.secondHalfStart,
                    it.round,
                    it.status,
                    it.statusShort,
                    it.elapsed,
                    it.venue,
                    it.referee,
                    it.goalsHomeTeam,
                    it.goalsAwayTeam
                )
            }
            db.getFixturesDao().saveAllFixtures(fixturesListDb!!)
        }
    }
}