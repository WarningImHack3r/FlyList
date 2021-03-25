package com.antoine.flylist.data.api

import android.os.Build
import com.antoine.flylist.FlyListApplication.Companion.context
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class APIManager {
    companion object {
        private val cache = Cache(File(context?.cacheDir, "responses"), 10 * 1024 * 1024)

        val flightAPI: FlightAPI = Retrofit.Builder()
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

        val aircraftAPI: AircraftAPI = Retrofit.Builder()
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

        fun epochToDate(epoch: String): String {
            return if (Build.VERSION.SDK_INT >= 26) {
                DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(epoch.toLong()))
            } else {
                SimpleDateFormat.getDateTimeInstance().format(Date(epoch.toLong() * 1000))
            }.replace("T", " ").replace("Z", " ").trim()
        }

        fun dateToEpoch(date: String): String {
            return SimpleDateFormat.getDateTimeInstance().parse(date)?.time.toString()
        }
    }
}
