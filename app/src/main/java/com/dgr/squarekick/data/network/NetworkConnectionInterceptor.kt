package com.dgr.squarekick.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.dgr.squarekick.BuildConfig
import com.dgr.squarekick.R
import com.dgr.squarekick.utils.NoInternetConnection
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(context: Context) : Interceptor {

    private val appContext = context.applicationContext

    private val headerHost = "x-rapidapi-host"
    private val propertyHost = "api-football-v1.p.rapidapi.com"

    private val headerKey = "x-rapidapi-key"

    private val headerAccept = "Accept"
    private val propertyAccept = "application/json"

    override fun intercept(chain: Interceptor.Chain): Response {

        if (!isInternetAvailable()) {
            throw NoInternetConnection(appContext.resources.getString(R.string.no_internet_error_message))
        }

        val request = chain.request().newBuilder()
            .addHeader(headerHost, propertyHost)
            .addHeader(headerKey, BuildConfig.ApiKey)
            .addHeader(headerAccept, propertyAccept)
            .build()

        return chain.proceed(request)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}