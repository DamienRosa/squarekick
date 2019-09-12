package com.dgr.squarekick.ui.home

import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.leagues.Leagues

interface HomeListener {
    fun onEmptyList()
    fun onFeedLayout(fixtures: Map<Leagues, List<Fixtures>>)
}