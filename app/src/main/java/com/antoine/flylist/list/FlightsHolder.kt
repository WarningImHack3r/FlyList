package com.antoine.flylist.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antoine.flylist.R

class FlightsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var cell : TextView = itemView.findViewById(R.id.flight_cell)
}
