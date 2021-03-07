package com.antoine.flylist.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.flylist.R
import com.antoine.flylist.data.Flight

class FlightsAdapter(private var dataSet : Array<Flight>) : RecyclerView.Adapter<FlightsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightsHolder {
        return FlightsHolder(LayoutInflater.from(parent.context).inflate(R.layout.flight_row, parent, false))
    }

    override fun onBindViewHolder(flightsHolder: FlightsHolder, position: Int) {
        flightsHolder.cell.text = dataSet[position].icao24
    }

    fun updateList(list: Array<Flight>) {
        dataSet = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}
