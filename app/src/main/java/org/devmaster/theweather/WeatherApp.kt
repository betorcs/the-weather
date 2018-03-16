package org.devmaster.theweather

import android.app.Application
import org.devmaster.theweather.di.DaggerWeatherComponent
import org.devmaster.theweather.di.DatabaseModule
import org.devmaster.theweather.di.NetworkModule
import org.devmaster.theweather.di.WeatherComponent

class WeatherApp : Application() {

    companion object {
        private var instance: WeatherApp? = null
        fun get() = instance

        @JvmStatic
        lateinit var component: WeatherComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        component = DaggerWeatherComponent.builder()
                .databaseModule(DatabaseModule(this))
                .networkModule(NetworkModule())
                .build()

    }

}