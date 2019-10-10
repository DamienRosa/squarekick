package com.dgr.squarekick.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.utils.lazyDeferred

class GameDetailsViewModel(
    private val fixturesRepository: FixturesRepository,
    fid: Int
) : ViewModel() {

    val fixtureDetails by lazyDeferred {
        Log.e("GameDetailsViewModel", "fid $fid")
        fixturesRepository.getFixtureDetails(fid)
    }

}
