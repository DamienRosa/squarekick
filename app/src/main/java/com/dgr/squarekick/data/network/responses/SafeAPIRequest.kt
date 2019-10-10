package com.dgr.squarekick.data.network.responses

import android.util.Log
import com.dgr.squarekick.utils.NoInternetConnection
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeAPIRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            Log.i("SafeAPIRequest", response.body().toString())
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error code: ${response.code()}")

            Log.e("SafeAPIRequest", response.errorBody()?.string())
            throw NoInternetConnection(message.toString())
        }
    }
}