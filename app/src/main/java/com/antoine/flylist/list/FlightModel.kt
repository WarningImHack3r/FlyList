package com.antoine.flylist.list

import com.antoine.flylist.FlyListApplication.Companion.context
import com.antoine.flylist.R
import com.antoine.flylist.data.responses.Flight

sealed class FlightModel

data class FetchSuccess(val flightList: List<Flight>) : FlightModel()
object FetchLoading : FlightModel()
data class FetchError(val text: String? = context?.getString(R.string.an_error_occurred_while_loading_data)) :
    FlightModel()
