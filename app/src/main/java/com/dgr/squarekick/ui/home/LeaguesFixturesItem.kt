package com.dgr.squarekick.ui.home

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.dgr.squarekick.R
import com.dgr.squarekick.databinding.ItemCompetitionBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.xwray.groupie.databinding.BindableItem

class LeaguesFixturesItem(
    val league: LeaguesFixtures
) : BindableItem<ItemCompetitionBinding>() {

    override fun bind(viewBinding: ItemCompetitionBinding, position: Int) {
        viewBinding.leagueData = league
        if (league.url != null && league.url.isNotEmpty()) {
            GlideToVectorYou
                .init()
                .with(viewBinding.ivCountryFlag.context.applicationContext)
                .load(Uri.parse(league.url), viewBinding.ivCountryFlag)
        }
    }

    override fun getLayout(): Int = R.layout.item_competition

    @BindingAdapter("url")
    fun loadImage(imageView: ImageView, imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            GlideToVectorYou
                .init()
                .with(imageView.context.applicationContext)
                .load(Uri.parse(imageUrl), imageView)
        }
    }
}