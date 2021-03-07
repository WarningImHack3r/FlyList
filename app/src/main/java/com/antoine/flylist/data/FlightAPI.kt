package com.antoine.flylist.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightAPI {
    @GET("api")
    fun listFlights(/*@Query("arrival") arrival: String*/): Call<List<Flight>>
}
