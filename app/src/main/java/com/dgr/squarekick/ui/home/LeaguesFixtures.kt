package com.dgr.squarekick.ui.home

import com.dgr.squarekick.data.network.responses.leagues.Leagues

data class LeaguesFixtures(
    val league: Leagues,
    val url : String?,
    val activeFixtures: Int,
    val totalFixtures: Int
)