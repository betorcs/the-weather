package org.devmaster.theweather.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import org.devmaster.theweather.model.CurrentWeather

@TypeConverters(Converters::class)
@Database(entities = [CurrentWeather::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

}