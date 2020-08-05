package com.example.weatherlogger.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Temperature(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("temp")
    var temp: Double
)
