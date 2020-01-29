package com.dgr.squarekick.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dgr.squarekick.R
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

class HomeFragment : Fragment(), KodeinAware {

    private val lAdapter: GroupAdapter<ViewHolder> by lazy { GroupAdapter<ViewHolder>() }
    private val factory: HomeViewModelFactory by instance()

    private val viewModel: HomeViewModel by lazy { ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)  }

    override val kodein by kodein()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val todayDate = LocalDateTime.now()
        bindViewModel(todayDate)
        bindNewUI(todayDate)
    }

    private fun bindNewUI(date: LocalDateTime) {
        createDatePicker(date)
        initRecyclerView()
    }

    private fun bindViewModel(date: LocalDateTime) {
        val formattedDate = date.format(DateTimeFormatter.ISO_DATE)
        viewModel.fetchLeaguesFixturesByDate(formattedDate)
        viewModel.leagueFixtures.observe(this, Observer {
            lAdapter.update(it)
            lAdapter.setOnItemClickListener { item, view ->
                val league = (item as LeaguesFixturesItem).league.league
                val extras = bundleOf(
                    EXTRA_LEAGUE to league,
                    EXTRA_FIXTURE_LIST to viewModel.getFixture(league),
                    EXTRA_DATE to formattedDate
                )

                view.findNavController()
                    .navigate(R.id.action_menu_home_to_leagueFixturesFragment, extras)
            }
        })

        viewModel.progressBar.observe(this, Observer {
            if (!it) {
                hideProgressBar()
            } else {
                showProgressBar()
            }
        })

        viewModel.emptyListMessage.observe(this, Observer {
            if (it) {
                tv_empty_list.show(false)
            } else {
                tv_empty_list.hide(false)
            }
        })
    }

    private fun initRecyclerView() {
        rv_competitions.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = lAdapter
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
                val newDate = LocalDateTime.of(year, month + 1, day, 0, 0)
                viewModel.fetchLeaguesFixturesByDate(newDate.format(DateTimeFormatter.ISO_DATE))
            }

            override fun onDisabledDateSelected(
                year: Int, month: Int, day: Int, dayOfWeek: Int, isDisabled: Boolean
            ) {
            }

        })
    }

    private fun hideProgressBar() {
        fl_progress_bar.hide(false)
        spin_kit.hide(false)
    }

    private fun showProgressBar() {
        fl_progress_bar.show(false)
        spin_kit.show(false)
    }

    companion object {
        const val EXTRA_LEAGUE = "league_extra"
        const val EXTRA_FIXTURE_LIST = "fixtures_list_extra"
        const val EXTRA_DATE = "date_extra"

    }
}
