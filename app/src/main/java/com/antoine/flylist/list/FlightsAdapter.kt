package com.antoine.flylist.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.flylist.FlyListApplication.Companion.context
import com.antoine.flylist.R
import com.antoine.flylist.data.api.APIManager
import com.antoine.flylist.data.responses.Flight
import com.antoine.flylist.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.logging.Logger

class FlightsAdapter(private var dataSet : Array<Flight>) : RecyclerView.Adapter<FlightsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightsHolder {
        return FlightsHolder(LayoutInflater.from(parent.context).inflate(R.layout.flight_row, parent, false))
    }

    override fun onBindViewHolder(flightsHolder: FlightsHolder, position: Int) {
        val flight = dataSet[position]
        APIManager.UTILITIES_API.airlineLogo(flight.icao24).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful && response.body() != null && response.body() != "n/a") {
                    Utils.loadImageFromURL(
                        response.body()!!,
                        flightsHolder.itemView.context,
                        flightsHolder.airline
                    )
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Logger.getGlobal().severe(t.stackTraceToString())
            }

        })
        flightsHolder.aircraft.text = flight.icao24.toUpperCase(Locale.ROOT)
        flightsHolder.departure.text = context?.getString(
            R.string.departure_time,
            Utils.epochToReadableDate(flight.firstSeen)
        )
        flightsHolder.arrival.text = context?.getString(
            R.string.arrival_time,
            Utils.epochToReadableDate(flight.lastSeen)
        )
    }

    override fun getItemCount() = dataSet.size

    fun updateList(list: Array<Flight>) {
        dataSet = list
        notifyDataSetChanged()
    }

    fun isEmpty() = dataSet.isEmpty()
}
