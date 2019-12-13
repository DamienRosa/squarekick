package com.dgr.squarekick.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgr.squarekick.data.network.responses.fixtures.Fixtures
import com.dgr.squarekick.data.network.responses.leagues.Leagues
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.utils.NoInternetConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val fixturesRepository: FixturesRepository) : ViewModel() {

    private var response : Map<Leagues, List<Fixtures>> = emptyMap()

    private val mLeagueFeatureItems = MutableLiveData<List<LeaguesFixturesItem>>()
    val leagueFixtures: LiveData<List<LeaguesFixturesItem>> = mLeagueFeatureItems

    private val mProgressBar = MutableLiveData<Boolean>()
    val progressBar : LiveData<Boolean> = mProgressBar

    private val mEmptyListMessage = MutableLiveData<Boolean>()
    val emptyListMessage : LiveData<Boolean> = mEmptyListMessage

    fun fetchLeaguesFixturesByDate(date: String) {
        try {
            viewModelScope.launch {
                mProgressBar.postValue(true)
                response = withContext(Dispatchers.IO) {
                    fixturesRepository.fetchLeaguesByFixtures(date)
                }

                if (response.isEmpty()) {
                    mEmptyListMessage.postValue(true)
                }

                mProgressBar.postValue(false)
                mLeagueFeatureItems.value= response.toFixtureItem()
            }
        } catch (e: Exception) {
            mProgressBar.postValue(false)
            // General Exception
        } catch (e: NoInternetConnection) {
            mProgressBar.postValue(false)
            // No InternetException
        }
    }

    fun getFixture(leagues: Leagues) = response[leagues]

    private fun Map<Leagues, List<Fixtures>>.toFixtureItem(): List<LeaguesFixturesItem> {
        return this.map { it ->
            LeaguesFixturesItem(
                LeaguesFixtures(
                    it.key,
                    if (!it.key.flag.isNullOrEmpty()) it.key.flag!! else it.key.logo,
                    it.value.count { activeGameStatusList.contains(it.statusShort) },
                    it.value.count()
                )
            )
        }
    }

    companion object{
        private val activeGameStatusList = listOf("1H", "HT", "2H", "ET", "P", "BT")
    }
}
