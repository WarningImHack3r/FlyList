package com.antoine.flylist.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UtilitiesAPI {
    @GET("ICAO-airport.php")
    fun airportFromIcao(@Query("icao") airportIcao: String): Call<String>

    @GET("hex-logo.php")
    fun airlineLogo(@Query("hex") icao: String): Call<String>
}
