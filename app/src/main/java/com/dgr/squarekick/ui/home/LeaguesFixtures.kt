package com.dgr.squarekick.ui.home

import com.dgr.squarekick.data.network.responses.leagues.Leagues


data class LeaguesFixtures(
    val league: Leagues,
    val url : String? = "http://www.akprzemyska.pl/wp-content/plugins/photo-gallery/css/bwg-fonts/fonts/bwg-fonts-svg/flag-o.svg",
    val activeFixtures: Int,
    val totalFixtures: Int
)