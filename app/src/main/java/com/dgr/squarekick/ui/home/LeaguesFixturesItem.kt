package com.dgr.squarekick.ui.home

import com.dgr.squarekick.R
import com.dgr.squarekick.databinding.ItemCompetitionBinding
import com.xwray.groupie.databinding.BindableItem

class LeaguesFixturesItem(
    private val league: LeaguesFixtures
) : BindableItem<ItemCompetitionBinding>() {
    override fun bind(viewBinding: ItemCompetitionBinding, position: Int) {
        viewBinding.leagueData = league
    }

    override fun getLayout(): Int = R.layout.item_competition
}