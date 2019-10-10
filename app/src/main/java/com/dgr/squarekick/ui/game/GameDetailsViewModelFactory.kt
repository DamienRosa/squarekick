package com.dgr.squarekick.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgr.squarekick.data.repositories.FixturesRepository

@Suppress("UNCHECKED_CAST")
class GameDetailsViewModelFactory(
    private val fixturesRepository: FixturesRepository,
    private val fid: Int)
    : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameDetailsViewModel(fixturesRepository, fid) as T
    }
}