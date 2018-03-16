package org.devmaster.theweather.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore

data class Temperature(

        @ColumnInfo(name = "temp_value")
        var temp: Float,

        @ColumnInfo(name = "temp_pressure")
        var pressure: Int,

        @ColumnInfo(name = "temp_humidity")
        var humidity: Int,

        @ColumnInfo(name = "temp_min")
        var tempMin: Float,

        @ColumnInfo(name = "temp_max")
        var tempMax: Float,

        @ColumnInfo(name = "temp_sea_level")
        var seaLevel: Float?,

        @ColumnInfo(name = "temp_grnd_level")
        var grndLevel: Float?)