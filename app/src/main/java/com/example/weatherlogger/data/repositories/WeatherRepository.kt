package com.example.weatherlogger.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherlogger.data.db.AppDatabase
import com.example.weatherlogger.data.db.entities.Temperature
import com.example.weatherlogger.data.models.WeatherResponse
import com.example.weatherlogger.data.network.MyApi
import com.example.weatherlogger.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository  (
    private val api: MyApi,
    private val db: AppDatabase

) : SafeApiRequest() {
    private val tempData = MutableLiveData<List<Temperature>>()


    fun saveTemp(temp: Temperature) {
        Coroutines.io {
            db.getTempDao().saveTemp(temp)
        }
    }


    suspend fun getAllTemp(): LiveData<List<Temperature>> {
        return withContext(Dispatchers.IO) {
            db.getTempDao().getTempData()
        }
    }


    suspend fun getTemp(lat:Double, lon:Double, appid :String):WeatherResponse {

        return apiRequest { api.getDataFirstly(lat, lon,appid) }
    }

}