package com.dgr.squarekick.ui.home

import androidx.lifecycle.ViewModel
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.utils.Coroutines
import com.dgr.squarekick.utils.NoInternetConnection
import com.dgr.squarekick.utils.lazyDeferred

class HomeViewModel(
    private val fixturesRepository: FixturesRepository
) : ViewModel() {

    var homeListener: HomeListener? = null

    val leagues by lazyDeferred {
        fixturesRepository.getLeaguesList()
    }

    fun fetchFixturesDate(date: String) {
        try {
            Coroutines.main {
                val fixturesList = fixturesRepository.fetchLeaguesByFixtures(date)
                if (fixturesList.isEmpty()) {
                    homeListener?.onEmptyList()
                    return@main
                }

                homeListener?.onFeedLayout(fixturesList)
                return@main
            }
        } catch (e: Exception) {
            homeListener?.onEmptyList()
        } catch (e: NoInternetConnection) {
            homeListener?.onEmptyList()
        }
    }
}
