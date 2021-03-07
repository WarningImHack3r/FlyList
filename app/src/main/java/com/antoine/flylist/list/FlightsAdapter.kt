package com.antoine.flylist.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antoine.flylist.R
import com.antoine.flylist.data.Flight

class FlightsAdapter(private val dataSet : Array<Flight>) : RecyclerView.Adapter<FlightsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightsHolder {
        return FlightsHolder(LayoutInflater.from(parent.context).inflate(R.layout.flight_row, parent, false))
    }

    override fun onBindViewHolder(flightsHolder: FlightsHolder, position: Int) {
        flightsHolder.cell.text = dataSet[position].name
    }

    override fun getItemCount() = dataSet.size
}
