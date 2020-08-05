package com.example.weatherlogger.ui.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherlogger.R
import com.example.weatherlogger.data.db.entities.Temperature
import com.example.weatherlogger.databinding.RowWeatherBinding


class WeatherAdapter ( private val tempList: List<Temperature>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>(){

    override fun getItemCount() = tempList.size

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
        holder.rowWeatherBinding.temperature = tempList[position]

    }


    inner class WeatherViewHolder(
        val rowWeatherBinding: RowWeatherBinding
    ) : RecyclerView.ViewHolder(rowWeatherBinding.root)

}