package com.antoine.flylist.list

import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.flylist.FlyListApplication.Companion.context
import com.antoine.flylist.data.responses.Flight
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

open class FlightViewModel(initialCall: Call<List<Flight>>) : ViewModel() {

    private val flightList: MutableLiveData<FlightModel> = MutableLiveData()
    open var call: Call<List<Flight>> = initialCall
        set(value) {
            field = value
            callApi()
        }

    private fun callApi() {
        flightList.value = FetchLoading
        call.clone().enqueue(object : Callback<List<Flight>> {
            override fun onResponse(call: Call<List<Flight>>, response: Response<List<Flight>>) {
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

    fun addListObserver(owner: LifecycleOwner, observer: (FlightModel) -> Unit) {
        flightList.observe(owner, observer)
    }
}
