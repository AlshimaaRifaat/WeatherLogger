package com.example.weatherlogger.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherlogger.data.db.entities.Temperature

@Dao
interface TemperatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTemp(temp : Temperature)

    @Query("SELECT * FROM Temperature")
    fun getTempData() : LiveData<List<Temperature>>

}