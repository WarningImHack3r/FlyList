package com.antoine.flylist.data

data class AllFlightResponse(
    val flights: Array<Flight>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AllFlightResponse

        if (!flights.contentEquals(other.flights)) return false

        return true
    }

    override fun hashCode(): Int {
        return flights.contentHashCode()
    }
}
