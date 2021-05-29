package com.antoine.flylist.data.responses

data class Aircraft(
    val ModeS: String, // Transponder address
    val Manufacturer: String, // Aircraft manufacturer
    val Type: String, // Aircraft model
    val ICAOTypeCode: String, // Formatted aircraft model
    val RegisteredOwners: String, // Owners
    val OperatorFlagCode: String // ICAO of the owners
)
