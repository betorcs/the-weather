package org.devmaster.theweather.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeather(

        @PrimaryKey
        var id: Long,

        var weather: ArrayList<Weather>,

        var base: String,

        @Embedded
        var main: Temperature,

        @Embedded
        var wind: Wind,

        var dt: Long,

        @Embedded
        var coord: Coordinate,

        @Embedded
        var sys: Sys,

        var name: String,

        var cod: Int)