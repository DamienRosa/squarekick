package com.dgr.squarekick.data.repositories

import com.dgr.squarekick.data.network.ApiFootballAPI
import com.dgr.squarekick.data.network.responses.SafeAPIRequest
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.fixtures.FixturesResponse
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.data.network.responses.leagues.LeaguesResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class FixturesRepository(
    private val api: ApiFootballAPI
) : SafeAPIRequest() {

    suspend fun fetchLeaguesByFixtures(date: String): Map<Leagues, List<Fixtures>> {
        return coroutineScope {
            val fixtures = async { fetchFixturesDate(date) }
            val leagues = async { fetchLeagues() }

            val leaguesList = leagues.await().api.leagues
            fixtures.await().let { it ->
                it.api.fixtures
                    .groupBy { leaguesList.find { _it -> _it.league_id == it.league_id }!! }
                    .toSortedMap(compareBy { it.country })
            }
        }
    }

    private suspend fun fetchFixturesDate(date: String): FixturesResponse {
        return apiRequest {
            api.getFixturesByDate(date)
        }
    }

    private suspend fun fetchLeagues(): LeaguesResponse {
        return apiRequest {
            api.getCompetitionsPerCountry()
        }
    }

}