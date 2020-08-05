package com.example.weatherlogger.data.network

import com.example.weatherlogger.data.models.WeatherResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MyApi {
    @GET("weather")
    suspend fun getWeatherList(@Query("lat") lat: Double,
                               @Query("lon") lon: Double,
                               @Query("appid") appid: String ) : Response<WeatherResponse>


    @GET("weather")
    suspend fun getDataFirstly(@Query("lat") lat: Double,
                               @Query("lon") lon: Double,
                               @Query("appid") appid: String ) : Response<WeatherResponse>
    companion object{
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): MyApi{
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .build()
                .create(MyApi::class.java)
        }
    }
}
