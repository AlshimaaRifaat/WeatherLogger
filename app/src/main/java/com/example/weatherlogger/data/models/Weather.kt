package com.example.weatherlogger.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Weather(

    @SerializedName("description")
    var description: String?,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("main")
    var main: String?
)