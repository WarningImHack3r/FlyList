package com.antoine.flylist.data.api

import com.antoine.flylist.data.responses.Aircraft
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AircraftAPI {
    @GET("aircraft/{icao}")
    fun aircraftFromIcao(@Path("icao") icao: String): Call<Aircraft>
}
