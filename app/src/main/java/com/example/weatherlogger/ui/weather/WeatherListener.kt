package com.example.weatherlogger.ui.weather

import com.example.weatherlogger.data.models.WeatherResponse


interface WeatherListener {
        fun onStarted()
        fun onSuccess(weatherResponse: WeatherResponse)
        fun onFailure(message:String)
}
