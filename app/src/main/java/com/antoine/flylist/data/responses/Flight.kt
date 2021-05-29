package com.antoine.flylist.data.responses

import android.os.Parcel
import android.os.Parcelable

data class Flight(
    val icao24: String?, // Unique 24-bit address of the transponder in hex
    var firstSeen: Long, // UNIX epoch timestamp of flight departure
    val estDepartureAirport: String?, // ICAO code of the departure airport
    var lastSeen: Long, // UNIX epoch timestamp of flight arrival
    val estArrivalAirport: String?, // ICAO code of the arrival airport
    val callsign: String?, // Callsign (identifier of the flight company)
    val estDepartureAirportHorizDistance: Int?, // Horizontal distance from the departure airport
    val estDepartureAirportVertDistance: Int?, // Vertical distance from the departure airport
    val estArrivalAirportHorizDistance: Int?, // Horizontal distance from the arrival airport
    val estArrivalAirportVertDistance: Int?, // Vertical distance from the arrival airport
    val departureAirportCandidatesCount: Int, // Number of possible departure airports
    val arrivalAirportCandidatesCount: Int // Number of possible arrival airports
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(icao24)
        parcel.writeLong(firstSeen)
        parcel.writeString(estDepartureAirport)
        parcel.writeLong(lastSeen)
        parcel.writeString(estArrivalAirport)
        parcel.writeString(callsign)
        parcel.writeValue(estDepartureAirportHorizDistance)
        parcel.writeValue(estDepartureAirportVertDistance)
        parcel.writeValue(estArrivalAirportHorizDistance)
        parcel.writeValue(estArrivalAirportVertDistance)
        parcel.writeInt(departureAirportCandidatesCount)
        parcel.writeInt(arrivalAirportCandidatesCount)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Flight> {
        override fun createFromParcel(parcel: Parcel): Flight {
            return Flight(parcel)
        }

        override fun newArray(size: Int): Array<Flight?> {
            return arrayOfNulls(size)
        }
    }
}
