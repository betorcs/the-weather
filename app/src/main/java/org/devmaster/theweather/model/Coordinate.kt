package org.devmaster.theweather.model

import android.arch.persistence.room.ColumnInfo

data class Coordinate(

        @ColumnInfo(name = "coord_lat")
        var lat: Double,

        @ColumnInfo(name = "coord_lon")
        var lon: Double)