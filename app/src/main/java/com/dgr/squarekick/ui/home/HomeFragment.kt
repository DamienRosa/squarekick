package com.dgr.squarekick.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dgr.squarekick.R
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.utils.Coroutines
import com.dgr.squarekick.utils.hide
import com.dgr.squarekick.utils.show
import com.vivekkaushik.datepicker.OnDateSelectedListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(), KodeinAware, HomeListener {

    private val viewModel: HomeViewModel by lazy {  ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)}

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

        viewModel.homeListener = this

        val todayDate = LocalDateTime.now()
        createDatePicker(todayDate)

        bindUI(todayDate)
    }

    private fun bindUI(todayDate: LocalDateTime) = Coroutines.main {
        showProgressBar()
        viewModel.leagues.await()
        viewModel.fetchFixturesDate(todayDate.format(DateTimeFormatter.ISO_DATE))
    }

    override fun onEmptyList() {
        hideProgressBar()
        tv_empty_list.show(false)
    }

    override fun onFeedLayout(date: String, fixtures: Map<Leagues, List<Fixtures>>) {
        hideProgressBar()
        initRecyclerView(date, fixtures)
    }

    private fun initRecyclerView(date: String, fixtures: Map<Leagues, List<Fixtures>>) {
        val lAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(fixtures.toFixtureItem())
        }
        lAdapter.setOnItemClickListener { item, view ->
            val league = (item as LeaguesFixturesItem).league.league
            val extras = bundleOf(
                EXTRA_LEAGUE to league,
                EXTRA_FIXTURE_LIST to fixtures[league],
                EXTRA_DATE to date
            )

            view.findNavController()
                .navigate(R.id.action_menu_home_to_leagueFixturesFragment, extras)
        }

        rv_competitions.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = lAdapter
        }
    }

    private fun hideProgressBar() {
        fl_progress_bar.hide(false)
        spin_kit.hide(false)
    }

    private fun showProgressBar() {
        fl_progress_bar.show(false)
        spin_kit.show(false)
    }

    private fun Map<Leagues, List<Fixtures>>.toFixtureItem(): List<LeaguesFixturesItem> {
        return this.map { it ->
            LeaguesFixturesItem(
                LeaguesFixtures(
                    it.key,
                    if (!it.key.flag.isNullOrEmpty()) it.key.flag else it.key.logo,
                    it.value.count { activeGameStatusList.contains(it.statusShort) },
                    it.value.count()
                )
            )
        }
    }

    private fun createDatePicker(todayDate: LocalDateTime) {
        datePickerTimeline.setInitialDate(
            todayDate.year,
            todayDate.month.value - 1,
            todayDate.dayOfMonth
        )
        datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                showProgressBar()
                val newDate = LocalDateTime.of(year, month+1, day, 0, 0)
                viewModel.fetchFixturesDate(newDate.format(DateTimeFormatter.ISO_DATE))
            }

            override fun onDisabledDateSelected(
                year: Int, month: Int, day: Int, dayOfWeek: Int, isDisabled: Boolean
            ) {
            }

        })
    }

    companion object {
        const val EXTRA_LEAGUE = "league_extra"
        const val EXTRA_FIXTURE_LIST = "fixtures_list_extra"
        const val EXTRA_DATE = "date_extra"

        private val activeGameStatusList = mutableListOf("1H", "HT", "2H", "ET", "P", "BT")
    }
}
