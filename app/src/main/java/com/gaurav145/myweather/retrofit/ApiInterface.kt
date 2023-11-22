package com.gaurav145.myweather.retrofit

import com.gaurav145.myweather.model.locationData.LocationData
import com.gaurav145.myweather.model.weatherData.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("direct")
    suspend fun getLocationDetails(
        @Query("q") q: String = "Nagpur",
        @Query("limit") limit: Int = 5,
        @Query("appid") appid: String = "4b920279c7592b0391cc08ff2d89c9c1"
    ): Response<LocationData>


    @GET("forecast")
    suspend fun getWeatherDetails(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,wind_speed_10m,wind_direction_10m"
    ): Response<WeatherData>
}
