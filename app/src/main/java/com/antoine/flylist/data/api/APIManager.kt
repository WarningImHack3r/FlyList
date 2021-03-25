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
        private val cache = Cache(File(context?.cacheDir, "responses"), 10 * 1024 * 1024)

        val FLIGHT_API: FlightAPI = Retrofit.Builder()
            .baseUrl("https://opensky-network.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient()
                    .newBuilder()
                    .cache(cache)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(FlightAPI::class.java)

        val AIRCRAFT_API: AircraftAPI = Retrofit.Builder()
            .baseUrl("https://api.joshdouch.me/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient()
                    .newBuilder()
                    .cache(cache)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(AircraftAPI::class.java)

        val UTILITIES_API: UtilitiesAPI = Retrofit.Builder()
            .baseUrl("https://api.joshdouch.me/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(
                OkHttpClient()
                    .newBuilder()
                    .cache(cache)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(UtilitiesAPI::class.java)
    }
}
