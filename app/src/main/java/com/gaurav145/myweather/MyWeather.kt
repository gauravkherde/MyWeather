package com.gaurav145.myweather

import android.app.Application
import com.facebook.stetho.Stetho

class MyWeather : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }

}
