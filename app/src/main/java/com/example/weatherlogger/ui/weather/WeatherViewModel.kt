package com.example.weatherlogger.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherlogger.util.Coroutines
import com.example.weatherlogger.data.models.Weather
import com.example.weatherlogger.data.repositories.WeatherRepository
import kotlinx.coroutines.Job

class WeatherViewModel (
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private lateinit var job: Job
    var weatherListener:WeatherListener?=null

    private val _weathers = MutableLiveData<List<Weather>>()
    val weathers: LiveData<List<Weather>>
        get() = _weathers

    fun getWeatherList(lat:Double,lon:Double,appid:String) {
        job = Coroutines.ioThenMain(
            { weatherRepository.getWeatherList(lat,lon,appid) },
            { _weathers.value = it?.weather}
        )
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
    }
}