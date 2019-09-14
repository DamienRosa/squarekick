package com.dgr.squarekick.ui.home

import android.annotation.TargetApi
import android.os.Build
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

    @TargetApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

        viewModel.homeListener = this

        val todayDate = LocalDateTime.now()
        datePickerTimeline.setInitialDate(
            todayDate.year,
            todayDate.month.value - 1,
            todayDate.dayOfMonth
        )
        datePickerTimeline.setOnDateSelectedListener(object : OnDateSelectedListener {
            override fun onDateSelected(year: Int, month: Int, day: Int, dayOfWeek: Int) {
                showProgressBar()
                val newDate = LocalDateTime.of(year, month, day, 0, 0)
                viewModel.fetchFixturesDate(newDate.format(DateTimeFormatter.ISO_DATE))
            }

            override fun onDisabledDateSelected(
                year: Int, month: Int, day: Int, dayOfWeek: Int, isDisabled: Boolean
            ) {
            }

        })

        bindUI()
    }


    @TargetApi(Build.VERSION_CODES.O)
    private fun bindUI() = Coroutines.main {
        showProgressBar()
        viewModel.leagues.await()
        val todayDate = LocalDateTime.now()
        viewModel.fetchFixturesDate(todayDate.format(DateTimeFormatter.ISO_DATE))
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
        lAdapter.setOnItemClickListener { _, _ ->
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
                    it.key.flag,
                    it.value.count { activeGameStatusList.contains(it.statusShort) },
                    it.value.count()
                )
            )
        }
    }
}
