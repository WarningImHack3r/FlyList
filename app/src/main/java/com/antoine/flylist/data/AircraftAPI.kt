package com.antoine.flylist.data

import retrofit2.Call
import retrofit2.http.GET

interface AircraftAPI {
    @GET("aircraft")
    fun aircraftFromIcao(/*query with '/' not '?'*/): Call<Aircraft>
}
