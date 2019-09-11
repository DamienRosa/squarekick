package com.dgr.squarekick.data.network

import com.dgr.squarekick.data.network.responses.FixturesResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiFootballAPI {

    @GET("fixtures/date/{date}")
    suspend fun getActiveFixtures(@Path("date") date: String) : Response<FixturesResponse>

    companion object {

        private const val BASE_API_URL = "https://api-football-v1.p.rapidapi.com/v2/"

        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor) : ApiFootballAPI {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiFootballAPI::class.java)
        }
    }
}