package org.devmaster.theweather.di

import dagger.Component
import org.devmaster.theweather.data.local.WeatherDao
import org.devmaster.theweather.data.network.WeatherApi
import org.devmaster.theweather.usecase.WeatherActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface WeatherComponent {

    fun inject(activity: WeatherActivity)

    fun getWeatherApi(): WeatherApi

    fun getWeatherDao(): WeatherDao
}