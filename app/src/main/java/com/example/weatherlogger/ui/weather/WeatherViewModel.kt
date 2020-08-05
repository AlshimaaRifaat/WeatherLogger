package com.example.weatherlogger.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherlogger.data.db.entities.Temperature
import com.example.weatherlogger.util.Coroutines
import com.example.weatherlogger.data.repositories.WeatherRepository
import com.example.weatherlogger.util.lazyDeferred
import kotlinx.coroutines.Job

class WeatherViewModel (
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private lateinit var job: Job
    var weatherListener:WeatherListener?=null

    suspend fun getTemp(
        lat:Double,
        lon:Double,
        appid: String
    ) = weatherRepository.getTemp(lat,lon,appid)


    val tempretures by lazyDeferred {
        weatherRepository.getAllTemp()
    }

    suspend fun saveTemp(temp: Temperature) = weatherRepository.saveTemp(temp)

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}