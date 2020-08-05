package com.example.weatherlogger.data.models


import com.example.weatherlogger.data.db.entities.Temperature
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("dt")
    var dt: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("main")
    var temperature: Temperature

)