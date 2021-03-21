package com.antoine.flylist.data

data class Aircraft(
    // TODO: nullable?, finish implementation, search for airport API
    // wait for prof tutorial for another API + images?
    val ModeS: String,
    val Manufacturer: String,
    val Type: String,
    val ICAOTypeCode: String,
    val RegisteredOwners: String,
    val OperatorFlagCode: String
)
