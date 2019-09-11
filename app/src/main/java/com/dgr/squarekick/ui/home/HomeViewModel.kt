package com.dgr.squarekick.ui.home

import androidx.lifecycle.ViewModel
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.utils.Coroutines

class HomeViewModel(
    private val fixturesRepository: FixturesRepository
) : ViewModel() {

    fun fetchFixturesDate(date: String) {
        Coroutines.main {
            var fixturesList = fixturesRepository.fetchFixturesDate(date)
            return@main
        }
    }
}
