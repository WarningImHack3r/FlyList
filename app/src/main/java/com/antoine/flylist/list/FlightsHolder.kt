package com.antoine.flylist.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antoine.flylist.R

class FlightsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var aircraft: TextView = itemView.findViewById(R.id.aircraft)
    var departure: TextView = itemView.findViewById(R.id.departure)
    var arrival: TextView = itemView.findViewById(R.id.arrival)
}
