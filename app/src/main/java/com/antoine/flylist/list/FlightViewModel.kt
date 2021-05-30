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
            if (flightList.hasObservers()) callApi()
        }

    private fun callApi() {
        flightList.value = FetchLoading
        Logger.getGlobal().severe(call.request().url().toString())
        call.clone().enqueue(object : Callback<List<Flight>> {
            override fun onResponse(call: Call<List<Flight>>, response: Response<List<Flight>>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        flightList.value = FetchSuccess(response.body()!!)
                    } else {
                        flightList.value = FetchError(response.message())
                        Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show()
                    }
                } else {
                    val errorString =
                        response.errorBody()!!.string().replace("[", "").replace("]", "")
                            .replace("\"", "")
                    flightList.value = FetchError(errorString)
                    Toast.makeText(context, errorString, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Flight>>, t: Throwable) {
                flightList.value = FetchError(t.localizedMessage)
                Toast.makeText(
                    context,
                    t.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                Logger.getGlobal().severe(t.stackTraceToString())
            }
        })
    }

    fun registerObserver(owner: LifecycleOwner, observer: (FlightModel) -> Unit) {
        flightList.observe(owner, observer)
    }
}
