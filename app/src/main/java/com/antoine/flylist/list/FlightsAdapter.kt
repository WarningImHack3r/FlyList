package com.antoine.flylist.list

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.flylist.MainActivity
import com.antoine.flylist.R
import com.antoine.flylist.data.Flight

class FlightsAdapter(private var dataSet : Array<Flight>) : RecyclerView.Adapter<FlightsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightsHolder {
        return FlightsHolder(LayoutInflater.from(parent.context).inflate(R.layout.flight_row, parent, false))
    }

    override fun onBindViewHolder(flightsHolder: FlightsHolder, position: Int) {
        val flight = dataSet[position]
        flightsHolder.aircraft.text = flight.icao24
        flightsHolder.departure.text = Resources.getSystem().getString(
            R.string.departure_time,
            MainActivity.epochToDate(flight.firstSeen.toString())
        )
        flightsHolder.arrival.text = Resources.getSystem()
            .getString(R.string.arrival_time, MainActivity.epochToDate(flight.lastSeen.toString()))
    }

    override fun getItemCount() = dataSet.size

    fun updateList(list: Array<Flight>) {
        dataSet = list
        notifyDataSetChanged()
    }

    fun isEmpty() = dataSet.isEmpty()
}
