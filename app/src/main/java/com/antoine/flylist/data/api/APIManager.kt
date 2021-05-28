package com.antoine.flylist.data.api

import com.antoine.flylist.FlyListApplication.Companion.context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class APIManager {
    companion object {
        private val client = OkHttpClient()
            .newBuilder()
            .cache(Cache(File(context?.cacheDir, "responses"), 10 * 1024 * 1024))
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        val FLIGHT_API: FlightAPI = Retrofit.Builder()
            .baseUrl("https://opensky-network.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(FlightAPI::class.java)

        val AIRCRAFT_API: AircraftAPI = Retrofit.Builder()
            .baseUrl("https://api.joshdouch.me/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AircraftAPI::class.java)

        val UTILITIES_API: UtilitiesAPI = Retrofit.Builder()
            .baseUrl("https://api.joshdouch.me/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()
            .create(UtilitiesAPI::class.java)
    }
}
