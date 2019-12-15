package com.dgr.squarekick.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgr.squarekick.data.network.responses.fixtures.FixturesResponse
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.utils.NoInternetConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDetailsViewModel(private val fixturesRepository: FixturesRepository) : ViewModel() {

    private val mFixturesDetails = MutableLiveData<FixturesResponse>()
    val fixturesResponse : LiveData<FixturesResponse> = mFixturesDetails

    fun fetchFixtureDetails(fid: Int) {
        try {
            viewModelScope.launch {
                val response = withContext(Dispatchers.IO) {
                    fixturesRepository.fetchFixtureById(fid)
                }

                mFixturesDetails.value = response
            }
        } catch (e: Exception) {
//            mProgressBar.postValue(false)
            // General Exception
        } catch (e: NoInternetConnection) {
//            mProgressBar.postValue(false)
            // No InternetException
        }
    }
}
