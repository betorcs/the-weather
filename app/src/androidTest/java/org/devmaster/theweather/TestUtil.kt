package org.devmaster.theweather

import org.devmaster.theweather.model.*


object TestUtil {


    fun createCurrentWeather(id: Long): CurrentWeather {
        val weather: ArrayList<Weather> = ArrayList()
        val temperature = Temperature(32.4F, 1, 1, 30F, 34F, null, null)
        val wind = Wind(20F, 90)
        val dt = System.currentTimeMillis() / 1000
        val coordinate = Coordinate(22.0, 72.0)
        val sys = Sys(1, 1, "None", "BR", 1L, 2L)
        return CurrentWeather(id, weather, "BAURU", temperature, wind, dt, coordinate, sys, "NAME", 200)
    }

}