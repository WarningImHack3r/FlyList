package com.antoine.flylist.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightAPI {
    @GET("flights/all")
    fun listFlights(@Query("begin") begin: Int?, @Query("end") end: Int?): Call<List<Flight>>
}
