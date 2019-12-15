package com.dgr.squarekick.data.repositories

import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.fixtures.FixturesResponse
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.data.network.responses.leagues.LeaguesResponse

interface IFixturesRepository {
    suspend fun fetchLeaguesByFixtures(date: String): Map<Leagues, List<Fixtures>>

    suspend fun fetchFixturesDate(date: String): FixturesResponse

    suspend fun fetchLeagues(): LeaguesResponse

    suspend fun fetchFixtureById(fId: Int) : FixturesResponse

    fun saveLeagues(leagueList: List<Leagues>?)

    fun saveFixture(fixtures: List<Fixtures>?)
}