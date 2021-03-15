package com.antoine.flylist.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightAPI {
    @GET("flights/all")
    fun allFlights(@Query("begin") begin: Long, @Query("end") end: Long): Call<List<Flight>>

    @GET("flights/aircraft")
    fun flightsByAircraft(@Query("icao24") icao24: String, @Query("begin") begin: Long, @Query("end") end: Long): Call<List<Flight>>

    @GET("flights/arrival")
    fun arrivalsByAirport(@Query("airport") airport: String, @Query("begin") begin: Long, @Query("end") end: Long): Call<List<Flight>>

    @GET("flights/departure")
    fun departuresByAirport(@Query("airport") airport: String, @Query("begin") begin: Long, @Query("end") end: Long): Call<List<Flight>>

    @GET("tracks")
    fun tracks(@Query("icao24") icao24: String, @Query("time") time: Long): Call<List<Flight>> // don't use - error page
}
