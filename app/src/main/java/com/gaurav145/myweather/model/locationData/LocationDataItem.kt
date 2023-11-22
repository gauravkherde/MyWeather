package com.gaurav145.myweather.model.locationData

data class LocationDataItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String
)