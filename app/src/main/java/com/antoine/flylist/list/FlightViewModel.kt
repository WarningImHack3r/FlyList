package com.antoine.flylist.list

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.flylist.FlyListApplication.Companion.context
import com.antoine.flylist.data.responses.Flight
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class FlightViewModel(initialCall: Call<List<Flight>>) : ViewModel() {

    val flightList: MutableLiveData<FlightModel> = MutableLiveData()
    private var call: Call<List<Flight>>

    init {
        call = initialCall
        callApi()
    }

    fun setCall(newCall: Call<List<Flight>>) {
        call = newCall
        callApi()
    }

    private fun callApi() {
        flightList.value = FetchLoading
        call.clone().enqueue(object : Callback<List<Flight>> {
            override fun onResponse(call: Call<List<Flight>>, response: Response<List<Flight>>) {
                // Loading
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        flightList.value = FetchSuccess(response.body()!!)
                    } else {
                        flightList.value = FetchError(response.message())
                    }
                } else {
                    flightList.value = FetchError(
                        response.errorBody()!!.string().replace("[", "").replace("]", "")
                            .replace("\"", "")
                    )
                }
            }

            override fun onFailure(call: Call<List<Flight>>, t: Throwable) {
                flightList.value = FetchError()
                Toast.makeText(
                    context,
                    "Error: " + t.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                Logger.getGlobal().severe(t.stackTraceToString())
            }
        })
    }
}
