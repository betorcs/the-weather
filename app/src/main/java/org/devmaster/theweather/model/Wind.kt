package org.devmaster.theweather.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Ignore

data class Wind(

        @ColumnInfo(name = "wind_speed") var speed: Float,

        @ColumnInfo(name = "wind_deg") var deg: Int)