package com.dgr.squarekick.data.repositories

import com.dgr.squarekick.data.network.ApiFootballAPI
import com.dgr.squarekick.data.network.responses.FixturesResponse
import com.dgr.squarekick.data.network.responses.SafeAPIRequest

class FixturesRepository(
    private val api: ApiFootballAPI
) : SafeAPIRequest() {

    suspend fun fetchFixturesDate(date : String) : FixturesResponse {
        return apiRequest {
            api.getActiveFixtures(date)
        }
    }

}