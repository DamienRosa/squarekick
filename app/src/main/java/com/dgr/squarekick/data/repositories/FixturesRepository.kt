package com.dgr.squarekick.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dgr.squarekick.data.db.SquareKickDataBase
import com.dgr.squarekick.data.db.entites.LeagueEntity
import com.dgr.squarekick.data.network.ApiFootballAPI
import com.dgr.squarekick.data.network.responses.SafeAPIRequest
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.fixtures.FixturesResponse
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class FixturesRepository(
    private val api: ApiFootballAPI,
    private val db: SquareKickDataBase
) : SafeAPIRequest() {

    val leaguesList = MutableLiveData<List<Leagues>>()

    init {
        leaguesList.observeForever {
            saveLeagues(it)
        }
    }

    suspend fun fetchLeaguesByFixtures(date: String): Map<Leagues, List<Fixtures>> {
        return coroutineScope {
            val fixturesAsync = async { fetchFixturesDate(date) }

            val fixturesList = fixturesAsync.await().api

            if (leaguesList.value!!.isEmpty() || fixturesList.results <= 0){
                return@coroutineScope emptyMap<Leagues, List<Fixtures>>()
            }

            fixturesList.let { it ->
                it.fixtures
                    .groupBy { leaguesList.value!!.find { _it -> _it.league_id == it.league_id }!! }
                    .toSortedMap(compareBy { it.country })
            }
        }
    }

    suspend fun getLeaguesList() : LiveData<List<LeagueEntity>>{
        return withContext(Dispatchers.IO){
            fetchLeagues()
            db.getLeagueDao().getLeaguesList()
        }
    }

    private suspend fun fetchFixturesDate(date: String): FixturesResponse {
        return apiRequest {
            api.getFixturesByDate(date)
        }
    }

    private suspend fun fetchLeagues() {
        val response = apiRequest {
            api.getCompetitionsPerCountry()
        }
        leaguesList.postValue(response.api.leagues)
    }

    private fun saveLeagues(leagueList: List<Leagues>?) {
        Coroutines.io{
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
                    it.is_current)
            }
            db.getLeagueDao().saveAllLeagues(leaguesListDb!!)
        }
    }

}