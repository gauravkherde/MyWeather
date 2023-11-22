package com.gaurav145.myweather.model.weatherData

data class Current(
    val apparent_temperature: Double,
    val cloud_cover: Int,
    val interval: Int,
    val is_day: Int,
    val precipitation: Double,
    val rain: Double,
    val relative_humidity_2m: Int,
    val showers: Double,
    val temperature_2m: Double,
    val time: String,
    val wind_direction_10m: Int,
    val wind_speed_10m: Double
)