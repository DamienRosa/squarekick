package com.dgr.squarekick.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dgr.squarekick.R
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import kotlinx.android.synthetic.main.item_league_fixture.view.*
import java.text.SimpleDateFormat
import java.util.*


class FixturesAdapter(val context: Context?, private val fixturesList: ArrayList<Fixtures>) :
    RecyclerView.Adapter<FixturesAdapter.FixtureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureViewHolder {
        val inflater =
            LayoutInflater.from(context).inflate(R.layout.item_league_fixture, parent, false)
        return FixtureViewHolder(inflater)
    }

    override fun getItemCount(): Int = if (fixturesList.size <= 0) -1 else fixturesList.size

    override fun onBindViewHolder(holder: FixtureViewHolder, position: Int) {
        val fixture = fixturesList[position]
        holder.bindFixture(fixture, context)
    }

    class FixtureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val EXTRA_FIXTURE: String = "extra_fixture"
        private val activeGameStatusList = mutableListOf("1H", "HT", "2H", "ET", "P", "BT")
        private val finishedGameStatusList = mutableListOf("FT", "AET", "PEN")

        fun bindFixture(fixture: Fixtures, context: Context?) {
            itemView.setOnClickListener {
                Log.e("FixtureAdapter", "data ${fixture.fixture_id}")
                val extras = bundleOf(
                    EXTRA_FIXTURE to fixture.fixture_id
                )
                itemView.findNavController()
                    .navigate(R.id.action_leagueFixturesFragment_to_gameDetailsFragment, extras)
            }

            itemView.tv_home_team_name.text = fixture.homeTeam!!.team_name
            Glide.with(context!!).load(fixture.homeTeam.logo).into(itemView.iv_home_team_logo)
            itemView.tv_away_team_name.text = fixture.awayTeam!!.team_name
            Glide.with(context).load(fixture.awayTeam.logo).into(itemView.iv_away_team_logo)

            itemView.tv_competition_name.text = fixture.venue

            if (activeGameStatusList.contains(fixture.statusShort)
                || finishedGameStatusList.contains(fixture.statusShort)
            ) {
                itemView.cl_ongoing_game.visibility = View.VISIBLE
                itemView.cl_scheduled_time.visibility = View.GONE

                itemView.tv_game_event.text = fixture.status
                itemView.tv_score_away.text = fixture.goalsAwayTeam.toString()
                itemView.tv_score_home.text = fixture.goalsHomeTeam.toString()
                itemView.tv_time_elapsed.text = fixture.elapsed.toString()

                if (finishedGameStatusList.contains(fixture.statusShort)) {
                    itemView.cl_fixture.background =
                        context.resources.getDrawable(R.drawable.shape_inactive_game_element_fill)
                } else {
                    itemView.cl_fixture.background =
                        context.resources.getDrawable(R.drawable.shape_active_game_element_fill)
                }
            } else {
                itemView.cl_ongoing_game.visibility = View.GONE
                itemView.cl_scheduled_time.visibility = View.VISIBLE

                itemView.tv_scheduled_day.text = fixture.status
                itemView.tv_scheduled_time.text = getHours(fixture.event_timestamp)
                itemView.cl_fixture.background =
                    context.resources.getDrawable(R.drawable.shape_inactive_game_element_fill)
            }
        }

        @SuppressLint("SimpleDateFormat")
        private fun getHours(eventDate: Int): CharSequence? {
            return SimpleDateFormat("HH:mm").format(Date(eventDate.toLong()*1000L))
        }
    }

}