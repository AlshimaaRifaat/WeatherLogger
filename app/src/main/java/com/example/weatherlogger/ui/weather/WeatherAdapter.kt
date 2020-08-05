package com.example.weatherlogger.ui.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.R
import com.example.weatherlogger.data.models.Weather
import com.example.weatherlogger.databinding.RowWeatherBinding


class WeatherAdapter ( private val weathers: List<Weather>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>(){

    override fun getItemCount() = weathers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeatherViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_weather,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.rowWeatherBinding.weather = weathers[position]

    }


    inner class WeatherViewHolder(
        val rowWeatherBinding: RowWeatherBinding
    ) : RecyclerView.ViewHolder(rowWeatherBinding.root)

}