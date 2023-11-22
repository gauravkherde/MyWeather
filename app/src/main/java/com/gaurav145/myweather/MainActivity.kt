package com.gaurav145.myweather

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gaurav145.myweather.databinding.ActivityMainBinding
import com.gaurav145.myweather.model.weatherData.WeatherData
import com.gaurav145.myweather.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: HomeViewModel
    var latitude: Double = 0.00
    var longitude: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        getLocationData()
        getSearchDetails()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadImages(weatherData: WeatherData) {
        Log.d("xxxxx", weatherData.toString())
        Log.d("xxxxx1", weatherData.current.rain.toInt().toString())
        Log.d("xxxxx2", weatherData.current.showers.toInt().toString())


        if (weatherData.current.is_day == 1) {

            if (weatherData.current.rain.toInt() == 0) {

                if (weatherData.current.showers.toInt() == 0) {

                    binding.weatherImage.setImageDrawable(getDrawable(R.drawable.only_sun))
                } else {

                    binding.weatherImage.setImageDrawable(getDrawable(R.drawable.sun_shower))
                }
            } else {

                binding.weatherImage.setImageDrawable(getDrawable(R.drawable.sun_rain))
            }
        } else {
            if (weatherData.current.rain.toInt() == 0) {

                if (weatherData.current.showers.toInt() == 0) {

                    binding.weatherImage.setImageDrawable(getDrawable(R.drawable.night))
                } else {

                    binding.weatherImage.setImageDrawable(getDrawable(R.drawable.night_showe))
                }
            } else {
                binding.weatherImage.setImageDrawable(getDrawable(R.drawable.night_rain))

            }

        }

    }

    private fun getSearchDetails() {
        binding.searchBox.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER))) {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.getLocationData(binding.searchBox.text.toString())
                }
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.indeterminateBar.visibility = View.VISIBLE
                    hideKeyboard(currentFocus ?: View(applicationContext))
                }
                observeLocationDetails()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })

    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getLocationData() {
        lifecycleScope.launch {
            viewModel.getLocationData("Nagpur")
        }
        observeLocationDetails()

    }

    private fun observeLocationDetails() {
        viewModel.observeLocationDetails().observe(this, Observer {
            if (it != null && !it.isEmpty()) {
                latitude = roundOffDecimal(it[0].lat)
                longitude = roundOffDecimal(it[0].lon)
                getWeatherData()

            } else {
                Toast.makeText(this, "Enter correct city", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getWeatherData() {
        viewModel.getWeatherData(latitude, longitude)
        viewModel.observeWeatherDetails().observe(this, Observer {
            updateWeatherData(it)
            loadImages(it)
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateWeatherData(weatherData: WeatherData) {
        with(binding)
        {
            tempValue.text =
                (weatherData.current.temperature_2m).toString() + " " + weatherData.current_units.temperature_2m
            feelLikeTempValue.text =
                "Feels like " + (weatherData.current.apparent_temperature).toString() + " " + weatherData.current_units.temperature_2m
            relativeHumidityValue.text =
                (weatherData.current.relative_humidity_2m).toString() + " " + (weatherData.current_units.relative_humidity_2m).toString()
            if (weatherData.current.rain.equals("0.00")) {
                weatherValue.visibility = GONE
            }
            rainValue.text =
                (weatherData.current.rain.toString()) + " " + weatherData.current_units.rain
            showerValue.text =
                (weatherData.current.showers.toString()) + " " + weatherData.current_units.showers
            precipitationValue.text =
                (weatherData.current.precipitation.toString()) + " " + weatherData.current_units.rain
            windSpeedValue.text =
                (weatherData.current.wind_speed_10m.toString()) + " " + weatherData.current_units.wind_speed_10m
            windDirectionValue.text =
                (weatherData.current.wind_direction_10m.toString()) + " " + weatherData.current_units.wind_direction_10m
        }
        binding.indeterminateBar.visibility = View.GONE

    }

    private fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }

    companion object {
        const val BASE_URL_CITY = "https://api.openweathermap.org/geo/1.0/"
        const val BASE_URL_WEATHER = "https://api.open-meteo.com/v1/"
    }

}

