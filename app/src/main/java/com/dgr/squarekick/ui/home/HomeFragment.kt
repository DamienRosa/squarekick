package com.dgr.squarekick.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dgr.squarekick.R
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.utils.hide
import com.dgr.squarekick.utils.show
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class HomeFragment : Fragment(), KodeinAware, HomeListener {

    private val activeGameStatusList = mutableListOf("1H", "HT", "2H", "ET", "P", "BT")

    private lateinit var viewModel: HomeViewModel

    override val kodein by kodein()

    private val factory: HomeViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

        viewModel.homeListener = this
        bindUI()
    }

    private fun bindUI() {
        showProgressBar()
        viewModel.fetchFixturesDate("2019-09-07")
    }

    override fun onEmptyList() {
        hideProgressBar()
        tv_empty_list.show(false)
    }

    override fun onFeedLayout(fixtures: Map<Leagues, List<Fixtures>>) {
        hideProgressBar()
        initRecyclerView(fixtures)
    }

    private fun initRecyclerView(fixtures: Map<Leagues, List<Fixtures>>) {
        val lAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(fixtures.toFixtureItem())
        }

        rv_competitions.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = lAdapter
        }
    }

    private fun hideProgressBar() {
        fl_progress_bar.hide(true)
        spin_kit.hide(true)
    }

    private fun showProgressBar() {
        fl_progress_bar.show(true)
        spin_kit.show(true)
    }

    private fun Map<Leagues, List<Fixtures>>.toFixtureItem(): List<LeaguesFixturesItem> {
        return this.map { it ->
            LeaguesFixturesItem(
                LeaguesFixtures(
                    it.key,
                    it.value.count { activeGameStatusList.contains(it.status) },
                    it.value.count()
                )
            )
        }
    }
}
