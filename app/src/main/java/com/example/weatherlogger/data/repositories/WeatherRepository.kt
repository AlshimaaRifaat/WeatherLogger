package com.example.weatherlogger.data.repositories

import com.example.weatherlogger.data.network.MyApi

class WeatherRepository  (
    private val api: MyApi
) : SafeApiRequest() {

    suspend fun getWeatherList(lat:Double,lon:Double,appid :String) = apiRequest { api.getWeatherList(lat,lon,appid) }

}