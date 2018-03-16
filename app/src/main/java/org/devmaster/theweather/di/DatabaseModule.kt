package org.devmaster.theweather.di

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import org.devmaster.theweather.data.local.AppDatabase
import org.devmaster.theweather.data.local.WeatherDao

@Module
class DatabaseModule(private val applicationContext: Context) {

    @Provides
    fun providesApplicationContext() = applicationContext

    @Provides
    fun providesAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context,
                AppDatabase::class.java, "weather-database.db").build()
    }

    @Provides
    fun providesWeatherDao(appDatabase: AppDatabase): WeatherDao {
        return appDatabase.weatherDao()
    }


}