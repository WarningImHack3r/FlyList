package com.antoine.flylist.data

data class Flight(
    val icao24: String, // Unique 24-bit address of the transponder in hex
    val firstSeen: Int, // UNIX epoch timestamp of flight departure
    val estDepartureAirport: String?, // ICAO code of the departure airport
    val lastSeen: Int, // UNIX epoch timestamp of flight arrival
    val estArrivalAirport: String?, // ICAO code of the arrival airport
    val callsign: String?, // Callsign of the plane
    val estDepartureAirportHorizDistance: Int, // Horizontal distance from the departure airport
    val estDepartureAirportVertDistance: Int, // Vertical distance from the departure airport
    val estArrivalAirportHorizDistance: Int, // Horizontal distance from the arrival airport
    val estArrivalAirportVertDistance: Int, // Vertical distance from the arrival airport
    val departureAirportCandidatesCount: Int, // Number of possible departure airports
    val arrivalAirportCandidatesCount: Int // Number of possible arrival airports
)
