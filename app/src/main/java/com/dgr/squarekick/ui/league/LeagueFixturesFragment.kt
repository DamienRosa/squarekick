package com.dgr.squarekick.ui.league

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dgr.squarekick.R
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.ui.adapter.FixturesAdapter
import com.dgr.squarekick.ui.home.HomeFragment
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.fragment_league_fixtures.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class LeagueFixturesFragment : Fragment() {

    private lateinit var date: String
    private lateinit var fixturesList: ArrayList<Fixtures>
    private lateinit var league: Leagues

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_league_fixtures, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        league = arguments?.getParcelable(HomeFragment.EXTRA_LEAGUE) as Leagues
        date = arguments?.getString(HomeFragment.EXTRA_DATE, "NO DATE")!!
        fixturesList =
            arguments?.getParcelableArrayList<Fixtures>(HomeFragment.EXTRA_FIXTURE_LIST)!!

        val url = if (!league.flag.isNullOrEmpty()) league.flag else league.logo
        GlideToVectorYou
            .init()
            .with(this.context!!.applicationContext)
            .load(Uri.parse(url), iv_league_logo)
        tv_league_name.text = league.name

        val dateTime = LocalDate.parse(date)
        tv_fixtures_date.text = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        val fixturesAdapter = FixturesAdapter(this.context?.applicationContext, fixturesList)
        rv_fixtures.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = fixturesAdapter
        }
    }
}
