package org.devmaster.theweather.model

import android.arch.persistence.room.ColumnInfo

data class Weather(

        @ColumnInfo(name = "weather_id")
        var id: Int,

        @ColumnInfo(name = "weather_main")
        var main: String,

        @ColumnInfo(name = "weather_description")
        var description: String,

        @ColumnInfo(name = "weather_icon")
        var icon: String) {

        val iconUrl: String = "http://openweathermap.org/img/w/$icon.png"

}