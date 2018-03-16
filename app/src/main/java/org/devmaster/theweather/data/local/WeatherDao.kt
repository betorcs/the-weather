package org.devmaster.theweather.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe
import org.devmaster.theweather.model.CurrentWeather

@Dao
interface WeatherDao {

    @Query("SELECT * FROM current_weather")
    fun getWeather(): Maybe<CurrentWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(current: CurrentWeather)

    @Query("DELETE FROM current_weather")
    fun deleteAll()

}