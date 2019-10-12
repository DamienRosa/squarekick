package com.dgr.squarekick.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dgr.squarekick.R
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.utils.Coroutines
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class GameDetailsFragment : Fragment(), KodeinAware {

    private var fixturesId: Int = 0

    private lateinit var viewModel: GameDetailsViewModel

    override val kodein by kodein()

    private val repository: FixturesRepository by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_game_details, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let { fixturesId = it.getInt(EXTRA_FIXTURE, 0) }

        viewModel = ViewModelProviders.of(this, GameDetailsViewModelFactory(repository, fixturesId)).get(GameDetailsViewModel::class.java)

        bindUI(fixturesId)
    }

    private fun bindUI(fixturesId: Int) = Coroutines.main {
//        showProgressBar()
        viewModel.fixtureDetails.await().observe(this, Observer {
            Log.e("GameDetailsFragment", "fid ${it[0].status}")
        })
    }
    companion object{
        const val EXTRA_FIXTURE: String = "extra_fixture"
    }
}
