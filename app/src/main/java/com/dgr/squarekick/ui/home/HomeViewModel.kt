package com.dgr.squarekick.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.utils.NoInternetConnection
import com.dgr.squarekick.utils.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val fixturesRepository: FixturesRepository) : ViewModel() {

    private val mFixtures =
        MutableLiveData<Map<Leagues, List<Fixtures>>>().apply { value = emptyMap() }
    val fixtures: LiveData<Map<Leagues, List<Fixtures>>> = mFixtures

    private val mProgressBar = MutableLiveData<Boolean>()
    val progressBar : LiveData<Boolean> = mProgressBar

    private val mEmptyListMessage = MutableLiveData<Boolean>()
    val emptyListMessage : LiveData<Boolean> = mEmptyListMessage

    fun fetchLeaguesFixturesByDate(date: String) {
        try {

            viewModelScope.launch {
                mProgressBar.postValue(true)
                val res = withContext(Dispatchers.IO) {
                    fixturesRepository.fetchLeaguesByFixtures(date)
                }

                if (res.isEmpty()) {
                    mProgressBar.postValue(false)
                    mEmptyListMessage.postValue(true)
                }

                mProgressBar.postValue(false)
                mFixtures.value = res
            }
        } catch (e: Exception) {
            mProgressBar.postValue(false)
            // General Exception
        } catch (e: NoInternetConnection) {
            mProgressBar.postValue(false)
            // No InternetException
        }
    }
}
