package com.gaurav145.myweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav145.myweather.MainActivity.Companion.BASE_URL_CITY
import com.gaurav145.myweather.MainActivity.Companion.BASE_URL_WEATHER
import com.gaurav145.myweather.model.locationData.LocationData
import com.gaurav145.myweather.model.weatherData.WeatherData
import com.gaurav145.myweather.retrofit.RetrofitInstance

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val locationLiveData = MutableLiveData<LocationData>()
    private val weatherLiveData = MutableLiveData<WeatherData>()

    suspend fun getLocationData(location:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance(BASE_URL_CITY).api.getLocationDetails(location)
            if (response.isSuccessful) {
                locationLiveData.postValue(response.body())
            }
        }


    }

    fun getWeatherData(lat : Double, log : Double)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherResponse = RetrofitInstance(BASE_URL_WEATHER).api.getWeatherDetails(lat,log)
            if(weatherResponse.isSuccessful && weatherResponse.body()!=null)
            {
                weatherLiveData.postValue(weatherResponse.body())
            }
        }
    }

    fun observeLocationDetails(): LiveData<LocationData> {

        return locationLiveData
    }

    fun observeWeatherDetails(): LiveData<WeatherData>
    {
        return  weatherLiveData
    }

}
